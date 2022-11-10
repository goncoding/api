package com.daesung.api.news.web;

import com.daesung.api.common.error.ErrorResource;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.NewsFile;
import com.daesung.api.news.domain.NewsThumbnailFile;
import com.daesung.api.news.domain.enumType.NbType;
import com.daesung.api.news.domain.enumType.ShowYn;
import com.daesung.api.news.repository.NewsFileRepository;
import com.daesung.api.news.repository.NewsRepository;
import com.daesung.api.news.repository.NewsThumbnailFileRepository;
import com.daesung.api.news.repository.condition.NewsSearchCondition;
import com.daesung.api.news.resource.NewsResource;
import com.daesung.api.news.validation.NewsValidation;
import com.daesung.api.news.web.dto.NewsDto;
import com.daesung.api.news.web.dto.NewsGetResponseDto;
import com.daesung.api.utils.image.AccessLogUtil;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.NasFileComponent;
import com.daesung.api.utils.upload.UploadFile;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.upload.UploadUtil.CHARSET_UTF8;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/news")

public class NewsController {

    @Value("${file.dir}")
    private String fileDir;


    String savePath = "/news";
    String whiteList = "hwp, pdf, pptx, ppt, xlsx, xls, xps, zip";

    String thumbWhiteList = "jpg, gif, png";

    private final ModelMapper modelMapper;
    private final FileStore fileStore;
    private final NewsRepository newsRepository;
    private final NewsThumbnailFileRepository newsThumbnailFileRepository;
    private final NewsFileRepository newsFileRepository;

    private final NewsValidation newsValidation;


    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity getNewsList(Pageable pageable,
                                      PagedResourcesAssembler<News> assembler,
                                      @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                      @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                      @RequestParam(name = "nbType", required = false, defaultValue = "") String nbType,
                                      @RequestParam(name = "page", required = false, defaultValue = "") String page,
                                      @RequestParam(name = "size", required = false, defaultValue = "") String size
    ) {
        //타입 검색 값 넣기
        NewsSearchCondition searchCondition = new NewsSearchCondition();

        if ("tit".equals(searchType)) {
            searchCondition.setSearchTitle(searchText);
        }
        if ("titCont".equals(searchText)) {
            searchCondition.setSearchTitle(searchText);
        }
        if ("NE".equals(nbType)) {
            searchCondition.setNbType(NbType.NE);
        }
        if ("RE".equals(nbType)) {
            searchCondition.setNbType(NbType.RE);
        }

        Page<News> newsPage = newsRepository.searchNewsList(searchCondition, pageable);
        PagedModel<EntityModel<News>> pagedModel = assembler.toModel(newsPage, e -> new NewsResource(e));



        return ResponseEntity.ok().body(pagedModel);
    }


    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity newsPost(
            @RequestPart(name = "newsDto") @Valid NewsDto newsDto,
            Errors errors,
            @RequestPart(required = false) MultipartFile thumbnailFile,
            @RequestPart(required = false) List<MultipartFile> newsFiles,
            Model model,
            @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        newsValidation.validate(newsDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        //뉴스
        if ("NE".equals(newsDto.getNbType())) {
            //문자열에서 html 태그 제거
            String content = newsDto.getContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

            News news = News.builder()
                    .nbType(NbType.NE)
                    .title(newsDto.getTitle())
                    .content(content)
                    .viewCnt(newsDto.getViewCnt())
                    .language(lang)
                    .build();

            News savedNews = newsRepository.save(news);
            System.out.println("savedNews = " + savedNews);

            //뉴스 섬네일 업로드
            if (thumbnailFile != null) {
                try {



                    UploadFile uploadFile = fileStore.storeFile(thumbnailFile, savePath, whiteList);

                    NewsThumbnailFile newsThumbnailFile = NewsThumbnailFile.builder()
                            .news(savedNews)
                            .thumbnailFileOriginalName(uploadFile.getOriginName())
                            .thumbnailFileSavedName(uploadFile.getNewName())
                            .thumbnailFileSavedPath(uploadFile.getRealPath())
                            .build();

                    newsThumbnailFileRepository.save(newsThumbnailFile);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //뉴스파일 업로드
            if (newsFiles != null) {
                try {

                    List<UploadFile> uploadFiles = fileStore.storeFileList(newsFiles, savePath, thumbWhiteList);

                    for (UploadFile uploadFile : uploadFiles) {

                        NewsFile newsFile = NewsFile.builder()
                                .news(savedNews)
                                .newsFileOriginalName(uploadFile.getOriginName())
                                .newsFileSavedName(uploadFile.getNewName())
                                .newsFileSavedPath(uploadFile.getRealPath())
                                .showYn(ShowYn.Y)
                                .build();

                        newsFileRepository.save(newsFile);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            URI uri = linkTo(NewsController.class, newsDto.getLanguage()).slash(savedNews.getId()).toUri();
            NewsResource newsResource = new NewsResource(savedNews);

            return ResponseEntity.created(uri).body(newsResource);

        }

        //보도
        if ("RE".equals(newsDto.getNbType())) {
            News news = News.builder()
                    .nbType(NbType.RE)
                    .title(newsDto.getTitle())
                    .newCompany(newsDto.getNewCompany())
                    .link(newsDto.getLink())
                    .viewCnt(newsDto.getViewCnt())
                    .language(lang)
                    .build();

            News savedNews = newsRepository.save(news);
            System.out.println("savedNews = " + savedNews);

            URI uri = linkTo(NewsController.class, newsDto.getLanguage()).slash(savedNews.getId()).toUri();
            NewsResource newsResource = new NewsResource(savedNews);
//            return null;
            return ResponseEntity.created(uri).body(newsResource);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("뉴스, 보도 타입을 확인해 주세요."));
    }


    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity getNews(
            @PathVariable("id") Long id,
            @PathVariable(name = "lang", required = true) String lang,
            @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
            @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
            @RequestParam(name = "nbType", required = false, defaultValue = "") String nbType,
             HttpServletRequest request,
             HttpSession session,
            Model model
    ) throws Exception {
        AccessLogUtil.fileViewAccessLog(request);
        String newsLang = "en";
        if(lang.equals("kr")) newsLang = "kr";

        News news;
        if (id == null) {
            news = new News();
        } else {
            Optional<News> optionalNews = newsRepository.findById(id);
            if (!optionalNews.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
            }
            news = optionalNews.get();
        }

        List<NewsThumbnailFile> getThumb = newsThumbnailFileRepository.findByNewsIdOrderByRegDateDesc(id);

        NewsThumbnailFile thumbnailFile;
        if (getThumb == null || getThumb.size() == 0) {
            thumbnailFile = null;
        }else{
            thumbnailFile = getThumb.get(0);
        }

        List<NewsFile> newsImgList = id == null ? null : newsFileRepository.findByNewsIdOrderByRegDateDesc(id);

        List<NewsThumbnailFile> thumbnailFileList = newsThumbnailFileRepository.findByNewsId(id);

        NewsGetResponseDto responseDto = NewsGetResponseDto.builder()
                .news(news)
                .newsThumbnailFile(thumbnailFile)
                .newsImgList(newsImgList)
                .build();

        return ResponseEntity.ok(responseDto);
    }



    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity newsPost(
            @PathVariable Long id,
            @RequestPart(name = "newsDto") @Valid NewsDto newsDto,
            Errors errors,
            @RequestPart(required = false) MultipartFile thumbnailFile,
            @RequestPart(required = false) List<MultipartFile> newsFiles,
            Model model,
            @PathVariable(name = "lang", required = true) String lang) {

        Optional<News> optionalNews = newsRepository.findById(id);
        if (!optionalNews.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        newsValidation.validate(newsDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        News news = optionalNews.get();

        //뉴스
        if ("NE".equals(newsDto.getNbType())) {

            news.setTitle(newsDto.getTitle());
            news.setContent(newsDto.getContent());

            News savedNews = newsRepository.save(news);

            System.out.println("savedNews = " + savedNews);

            //뉴스 섬네일 업로드
            if (thumbnailFile != null) {
                try {

                    UploadFile uploadFile = fileStore.storeFile(thumbnailFile, savePath, thumbWhiteList);
                    if (uploadFile.isWrongType()) {
                        return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                    }

                    NewsThumbnailFile newsThumbnailFile = NewsThumbnailFile.builder()
                            .news(savedNews)
                            .thumbnailFileOriginalName(uploadFile.getOriginName())
                            .thumbnailFileSavedName(uploadFile.getNewName())
                            .thumbnailFileSavedPath(uploadFile.getRealPath())
                            .build();

                    newsThumbnailFileRepository.save(newsThumbnailFile);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //뉴스파일 업로드
            if (newsFiles != null) {
                try {
                    List<UploadFile> uploadFiles = fileStore.storeFileList(newsFiles, savePath, whiteList);

                    for (UploadFile uploadFile : uploadFiles) {
                        if (uploadFile.isWrongType()) {
                            return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                        }
                    }

                    //display N 처리
                    newsFileRepository.updateShowN(id);

                    for (UploadFile uploadFile : uploadFiles) {
                        //최근 등록 건만 Y 처리
                        NewsFile newsFile = NewsFile.builder()
                                .news(savedNews)
                                .newsFileOriginalName(uploadFile.getOriginName())
                                .newsFileSavedName(uploadFile.getNewName())
                                .newsFileSavedPath(uploadFile.getRealPath())
                                .showYn(ShowYn.Y)
                                .build();

                        newsFileRepository.save(newsFile);

                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            NewsResource newsResource = new NewsResource(savedNews);

//            return ResponseEntity.ok(savedNews);
            return ResponseEntity.status(HttpStatus.OK).body(newsResource);
        }

        //보도
        if ("RE".equals(newsDto.getNbType())) {

            news.setTitle(newsDto.getTitle());
            news.setNewCompany(newsDto.getNewCompany());
            news.setLink(newsDto.getLink());

            News savedNews = newsRepository.save(news);

            System.out.println("savedNews = " + savedNews);
            System.out.println("savedNews = " + savedNews);

            URI uri = linkTo(NewsController.class, newsDto.getLanguage()).slash(savedNews.getId()).toUri();
            NewsResource newsResource = new NewsResource(savedNews);
//            return null;
            return ResponseEntity.ok(newsResource);
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("뉴스, 보도 타입을 확인해 주세요."));

    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteNews(@PathVariable Long id){
        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
        }

        List<NewsThumbnailFile> byNewsId = newsThumbnailFileRepository.findByNewsId(id);
        for (NewsThumbnailFile thumbnailFile : byNewsId) {

            String thumbnailFileSavedPath = thumbnailFile.getThumbnailFileSavedPath();

            File file = new File(thumbnailFileSavedPath);
            if (file.exists()) {
                file.delete();
            }
        }
        newsThumbnailFileRepository.deleteByNewsId(id);

        List<NewsFile> newsFiles = newsFileRepository.findByNewsId(id);
        for (NewsFile newsFile : newsFiles) {

            String newsFileSavedPath = newsFile.getNewsFileSavedPath();

            File file = new File(newsFileSavedPath);
            if (file.exists()) {
                file.delete();
            }
        }


        newsFileRepository.deleteByNewsId(id);

        newsRepository.deleteById(id);





        return ResponseEntity.ok(id+"번 삭제 성공");
    }


    @ResponseBody
    @GetMapping("/viewThumb/{newsSeq}")
    public ResponseEntity viewThumbnail(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable Long newsSeq) throws MalformedURLException {

        AccessLogUtil.fileViewAccessLog(request);
        NewsThumbnailFile thumbnailFile;
        if (newsSeq == null) {
            thumbnailFile = new NewsThumbnailFile();
        } else {
            List<NewsThumbnailFile> thumbList = newsThumbnailFileRepository.findByNewsIdOrderByRegDateDesc(newsSeq);
            if (thumbList == null || thumbList.size() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 썸네일 정보가 없습니다. 사용자 id를 확인해주세요."));
            }
            NewsThumbnailFile newsThumbnailFile = thumbList.get(0);
//            File serverFile = new File(fileDir + savePath + "/" + newsThumbnailFile.getThumbnailFileSavedName());
            File serverFile = new File(newsThumbnailFile.getThumbnailFileSavedPath());
            NasFileComponent.putFileToResponseStreamAsView(response, serverFile);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 썸네일 정보가 없습니다. 사용자 id를 확인해주세요."));
    }

    //image 출력 파일 가져오기
    @GetMapping("/viewImage/{folderName}/{fileName}")
    public void viewEditorImages(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String folderName,
            @PathVariable String fileName) {
        AccessLogUtil.fileViewAccessLog(request);

        File serverFile = new File(fileDir + savePath + "/" + folderName + "/" +fileName);
        NasFileComponent.putFileToResponseStreamAsView(response, serverFile);

    }




    //최근 썸네일 이미지 출력

//    @ResponseBody
//    @GetMapping("/viewImages/{newsSeq}")
//    public ResponseEntity viewImages(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            @PathVariable Long newsSeq) throws MalformedURLException {
//
//        AccessLogUtil.fileViewAccessLog(request);
//        NewsThumbnailFile thumbnailFile;
//        if (newsSeq == null) {
//            thumbnailFile = new NewsThumbnailFile();
//        } else {
//            List<NewsFile> thumbList = newsFileRepository.findByNewsIdAndShowYn(newsSeq, ShowYn.Y);
//
//            if (thumbList == null || thumbList.size() == 0) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 썸네일 정보가 없습니다. 사용자 id를 확인해주세요."));
//            }
//
//            for (NewsFile newsFile : thumbList) {
//                File serverFile = new File(newsFile.getNewsFileSavedPath());
//                NasFileComponent.putFileToResponseStreamAsView(response, serverFile);
//            }
////            NewsThumbnailFile newsThumbnailFile = thumbList.get(0);
////            File serverFile = new File(fileDir + savePath + "/" + newsThumbnailFile.getThumbnailFileSavedName());
////            File serverFile = new File(newsThumbnailFile.getThumbnailFileSavedPath());
////            NasFileComponent.putFileToResponseStreamAsView(response, serverFile);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 썸네일 정보가 없습니다. 사용자 id를 확인해주세요."));
//    }

    private ResponseEntity<ErrorResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }





}
