package com.daesung.api.news.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.news.upload.NewsFileStore;
import com.daesung.api.news.upload.NewsThumbFileStore;
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
import com.daesung.api.news.web.dto.NewsGetResponse;
import com.daesung.api.utils.image.AccessLogUtil;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.NasFileComponent;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/news")

public class NewsController {

    @Value("${file.dir}")
    private String fileDir;


    String savePath = "/news";
    String whiteList = "jpg,gif,png,hwp,pdf,pptx,ppt,xlsx,xls,xps,zip";

    String thumbWhiteList = "jpg, gif, png";

    private final ModelMapper modelMapper;

    private final NewsThumbFileStore newsThumbFileStore;

    private final NewsFileStore newsFileStore;

    private final FileStore fileStore;
    private final NewsRepository newsRepository;
    private final NewsThumbnailFileRepository newsThumbnailFileRepository;
    private final NewsFileRepository newsFileRepository;

    private final NewsValidation newsValidation;

    /**
     * (뉴스&보도) 리스트 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity getNewsList(Pageable pageable,
                                      PagedResourcesAssembler<News> assembler,
                                      @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                      @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                      @RequestParam(name = "nbType", required = false, defaultValue = "") String nbType,
                                      @RequestParam(name = "page", required = false, defaultValue = "") String page,
                                      @RequestParam(name = "size", required = false, defaultValue = "") String size) {
        //타입 검색 값 넣기
        NewsSearchCondition searchCondition = new NewsSearchCondition();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.getPrincipal();
        if ("tit".equals(searchType)) {
            searchCondition.setSearchTitle(searchText);
        }
        if ("cont".equals(searchType)) {
            searchCondition.setSearchText(searchText);
        }
        if ("NE".equals(nbType)) {
            searchCondition.setNbType(NbType.NE);
        }
        if ("RE".equals(nbType)) {
            searchCondition.setNbType(NbType.RE);
        }

        Page<News> news = newsRepository.searchNewsList(searchCondition, pageable);
        PagedModel<NewsResource> pagedModel = assembler.toModel(news, n -> new NewsResource(n));

        return ResponseEntity.ok().body(pagedModel);
    }

    /**
     * (뉴스&보도) 썸내일 리스트 조회
     */
    @GetMapping(value = "/thumbnail", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity getNewsThumbList(Pageable pageable,
                                      PagedResourcesAssembler<NewsThumbnailFile> assembler) {

        Sort sort = Sort.by("id").descending();

        List<NewsThumbnailFile> thumbnailFiles = newsThumbnailFileRepository.findAll(sort);

        return ResponseEntity.ok().body(thumbnailFiles);
    }



    /**
     * (뉴스&보도) 단건 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity newsPost(
            @RequestPart(name = "newsDto") @Valid NewsDto newsDto, Errors errors,
            @RequestPart(required = false) MultipartFile thumbnailFile,
            @RequestPart(required = false) List<MultipartFile> newsFiles,
            @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "(뉴스&보도) 단건 등록 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        newsValidation.validate(newsDto, errors);
        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "(뉴스&보도) 단건 등록 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        //뉴스
        if ("NE".equals(newsDto.getNbType())) {
            //문자열에서 html 태그 제거
//            String content = newsDto.getContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

            newsDto.setViewCnt(0);

            News news = News.builder()
                    .nbType(NbType.NE)
                    .title(newsDto.getTitle())
                    .content(newsDto.getContent())
                    .viewCnt(newsDto.getViewCnt())
                    .selectRegDate(newsDto.getSelectRegDate())
                    .language(lang)
                    .thumbnailFileSummary(newsDto.getThumbSummary())
                    .build();

            News savedNews = newsRepository.save(news);
            System.out.println("savedNews = " + savedNews);

            //뉴스 섬네일 업로드
            if (thumbnailFile != null) {
                try {

                    UploadFile uploadFile = fileStore.storeFile(thumbnailFile, savePath, thumbWhiteList);

                    if (uploadFile.isWrongType()) {
                        log.error("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                        return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                    }

                    NewsThumbnailFile newsThumbnailFile = NewsThumbnailFile.builder()
                            .news(savedNews)
                            .thumbnailFileOriginalName(uploadFile.getOriginName())
                            .thumbnailFileSavedName(uploadFile.getNewName())
                            .thumbnailFileSavedPath(uploadFile.getRealPath())
//                            .thumbnailFileSummary(newsDto.getThumbSummary())
                            .build();

                    newsThumbnailFileRepository.save(newsThumbnailFile);

                } catch (IOException e) {
                    return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
                }
            }

            //뉴스파일 업로드
            if (newsFiles != null) {
                try {

                    List<UploadFile> uploadFiles = fileStore.storeFileList(newsFiles, savePath, whiteList);

                    for (UploadFile uploadFile : uploadFiles) {
                        if (uploadFile.isWrongType()) {
                            log.error("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                            return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                        }

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
                    return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
                }
            }


            URI uri = linkTo(NewsController.class, newsDto.getLanguage()).slash(savedNews.getId()).toUri();
            NewsResource newsResource = new NewsResource(savedNews);


            return ResponseEntity.created(uri).body(newsResource);

        }

        //보도
        if ("RE".equals(newsDto.getNbType())) {

            if (newsFiles != null) {
                log.error("status = {}, message = {}", "400", "첨부파일은 뉴스만 가능합니다.");
                return ResponseEntity.badRequest().body(new ErrorResponse("첨부파일은 뉴스만 가능합니다.","400"));
            }

            newsDto.setViewCnt(0);

            News news = News.builder()
                    .nbType(NbType.RE)
                    .title(newsDto.getTitle())
                    .newCompany(newsDto.getNewCompany())
                    .link(newsDto.getLink())
                    .viewCnt(newsDto.getViewCnt())
                    .selectRegDate(newsDto.getSelectRegDate())
                    .language(lang)
                    .thumbnailFileSummary(newsDto.getThumbSummary())
                    .build();

            News savedNews = newsRepository.save(news);
            System.out.println("savedNews = " + savedNews);

            //뉴스 섬네일 업로드
            if (thumbnailFile != null) {
                try {

                    UploadFile uploadFile = fileStore.storeFile(thumbnailFile, savePath, thumbWhiteList);

                    if (uploadFile.isWrongType()) {
                        log.error("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                        return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                    }

                    NewsThumbnailFile newsThumbnailFile = NewsThumbnailFile.builder()
                            .news(savedNews)
                            .thumbnailFileOriginalName(uploadFile.getOriginName())
                            .thumbnailFileSavedName(uploadFile.getNewName())
                            .thumbnailFileSavedPath(uploadFile.getRealPath())
//                            .thumbnailFileSummary(newsDto.getThumbSummary())
                            .build();

                    newsThumbnailFileRepository.save(newsThumbnailFile);

                } catch (IOException e) {
                    return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
                }
            }

            URI uri = linkTo(NewsController.class, newsDto.getLanguage()).slash(savedNews.getId()).toUri();
            NewsResource newsResource = new NewsResource(savedNews);
//            return null;
            return ResponseEntity.created(uri).body(newsResource);
        }
        log.error("status = {}, message = {}", "400", "뉴스, 보도 타입을 확인해 주세요.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("뉴스, 보도 타입을 확인해 주세요."));
    }

    /**
     * news 단건 조회
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity getNews(@PathVariable("id") Long id,
                                  @PathVariable(name = "lang", required = true) String lang,
                                  Pageable pageable,
                                  @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                  @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                  @RequestParam(name = "nbType", required = false, defaultValue = "") String nbType,
                                  HttpServletRequest request,
                                  HttpSession session) throws Exception {

        //타입 검색 값 넣기
        NewsSearchCondition searchCondition = new NewsSearchCondition();

        if ("tit".equals(searchType)) {
            searchCondition.setSearchTitle(searchText);
        }
        if ("cont".equals(searchType)) {
            searchCondition.setSearchText(searchText);
        }
        if ("NE".equals(nbType)) {
            searchCondition.setNbType(NbType.NE);
        }
        if ("RE".equals(nbType)) {
            searchCondition.setNbType(NbType.RE);
        }

        News prevNews = newsRepository.searchPrevNews(id, searchCondition);
        News nextNews = newsRepository.searchNextNews(id, searchCondition);

        AccessLogUtil.fileViewAccessLog(request);

        String newsLang = "en";
        if(lang.equals("kr")) newsLang = "kr";

        News news;
        if (id == null) {
            news = new News();
        } else {
            Optional<News> optionalNews = newsRepository.findById(id);
            if (!optionalNews.isPresent()) {
                log.error("status = {}, message = {}", "404", "일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요.","400"));
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


        NewsGetResponse responseDto = NewsGetResponse.builder()
                .news(news)
                .newsThumbnailFile(thumbnailFile)
                .newsImgList(newsImgList)
                .prevNews(prevNews)
                .nextNews(nextNews)
                .build();

        return ResponseEntity.ok(responseDto);
    }


    /**
     * (뉴스&보도) 단건 수정
     */
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
            log.error("status = {}, message = {}", "404", "일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
        }

        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "(뉴스&보도) 단건 수정 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }


        newsValidation.validate(newsDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        News news = optionalNews.get();

        //뉴스
        if ("NE".equals(newsDto.getNbType())) {

            news.updateNews(newsDto);

            News savedNews = newsRepository.save(news);

            List<NewsThumbnailFile> thumbnailFiles = newsThumbnailFileRepository.findByNewsId(savedNews.getId());
//            for (NewsThumbnailFile file : thumbnailFiles) {
//                file.updateThumbnailSummary(newsDto.getThumbSummary());
//                newsThumbnailFileRepository.save(file);
//            }

            System.out.println("savedNews = " + savedNews);

            //뉴스 섬네일 업로드
            if (thumbnailFile != null) {
                try {

                    UploadFile uploadFile = newsThumbFileStore.storeFile(thumbnailFile, savePath, thumbWhiteList, id);
                    if (uploadFile.isWrongType()) {
                        log.error("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
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
                    return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
                }
            }

            //뉴스파일 업로드
            if (newsFiles != null) {
                try {
                    List<UploadFile> uploadFiles = newsFileStore.storeFileList(newsFiles, savePath, whiteList, id);

                    for (UploadFile uploadFile : uploadFiles) {
                        if (uploadFile.isWrongType()) {
                            log.error("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
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
                    return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
                }
            }

            NewsResource newsResource = new NewsResource(savedNews);

//            return ResponseEntity.ok(savedNews);
            return ResponseEntity.status(HttpStatus.OK).body(newsResource);
        }

        //보도
        if ("RE".equals(newsDto.getNbType())) {

            if (newsFiles != null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("보도는 뉴스 파일을 추가할 수 없습니다.","400"));
            }

            List<NewsFile> newsFileList = newsFileRepository.findByNewsId(id);
            if (newsFileList != null) {
                for (NewsFile newsFile : newsFileList) {

                    String newsFileSavedPath = fileDir + newsFile.getNewsFileSavedPath() + "/" + newsFile.getNewsFileSavedName();

                    File file = new File(newsFileSavedPath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                newsFileRepository.deleteByNewsId(id);
            }

            news.updateReport(newsDto);

            News savedNews = newsRepository.save(news);

            System.out.println("savedNews = " + savedNews);

            List<NewsThumbnailFile> thumbnailFiles = newsThumbnailFileRepository.findByNewsId(savedNews.getId());
//            for (NewsThumbnailFile file : thumbnailFiles) {
//                file.updateThumbnailSummary(newsDto.getThumbSummary());
//                newsThumbnailFileRepository.save(file);
//            }

            //보도 섬네일 업로드
            if (thumbnailFile != null) {
                try {

                    UploadFile uploadFile = newsThumbFileStore.storeFile(thumbnailFile, savePath, thumbWhiteList, id);
                    if (uploadFile.isWrongType()) {
                        log.error("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
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
                    return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
                }
            }

            URI uri = linkTo(NewsController.class, newsDto.getLanguage()).slash(savedNews.getId()).toUri();
            NewsResource newsResource = new NewsResource(savedNews);
//            return null;
            return ResponseEntity.ok(newsResource);
        }

        log.error("status = {}, message = {}", "400", "뉴스, 보도 타입을 확인해 주세요.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("뉴스, 보도 타입을 확인해 주세요."));

    }



    /**
     * (뉴스&보도) 단건 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteNews(@PathVariable Long id,
                                     @PathVariable(name = "lang", required = true) String lang){

        Optional<News> optionalNews = newsRepository.findById(id);
        if (!optionalNews.isPresent()) {
            log.error("status = {}, message = {}", "404", "일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
        }

        List<NewsThumbnailFile> byNewsId = newsThumbnailFileRepository.findByNewsId(id);
        for (NewsThumbnailFile thumbnailFile : byNewsId) {

            String thumbnailFileSavedPath = fileDir + thumbnailFile.getThumbnailFileSavedPath() + "/" + thumbnailFile.getThumbnailFileSavedName();

            File file = new File(thumbnailFileSavedPath);
            if (file.exists()) {
                file.delete();
            }
        }
        newsThumbnailFileRepository.deleteByNewsId(id);

        List<NewsFile> newsFiles = newsFileRepository.findByNewsId(id);
        for (NewsFile newsFile : newsFiles) {

            String newsFileSavedPath = fileDir + newsFile.getNewsFileSavedPath() + "/" + newsFile.getNewsFileSavedName();

            File file = new File(newsFileSavedPath);
            if (file.exists()) {
                file.delete();
            }
        }
        newsFileRepository.deleteByNewsId(id);

        newsRepository.deleteById(id);

        return ResponseEntity.ok(id+"번 삭제 성공");
    }

    /**
     * (뉴스&보도) 썸네일 이미지 출력
     */
    @ResponseBody
    @GetMapping("/viewThumb/{newsId}")
    public ResponseEntity viewThumbnail(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable Long newsId) throws MalformedURLException {

        AccessLogUtil.fileViewAccessLog(request);
        NewsThumbnailFile thumbnailFile;

        if (newsId == null) {
            thumbnailFile = new NewsThumbnailFile();
        } else {
            List<NewsThumbnailFile> thumbList = newsThumbnailFileRepository.findByNewsIdOrderByRegDateDesc(newsId);

            if (thumbList == null || thumbList.size() == 0) {
                log.error("status = {}, message = {}", "404", "일치하는 썸네일 정보가 없습니다. 사용자 id를 확인해주세요.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 썸네일 정보가 없습니다. 사용자 id를 확인해주세요."));
            }
            NewsThumbnailFile newsThumbnailFile = thumbList.get(0);
//            File serverFile = new File(fileDir + savePath + "/" + newsThumbnailFile.getThumbnailFileSavedName());
            String pathname = newsThumbnailFile.getThumbnailFileSavedPath() + "/" + newsThumbnailFile.getThumbnailFileSavedName();
            File serverFile = new File(pathname);
            NasFileComponent.putFileToResponseStreamAsView(response, serverFile);
        }
        log.error("status = {}, message = {}", "404", "일치하는 썸네일 정보가 없습니다. 사용자 id를 확인해주세요.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 썸네일 정보가 없습니다. 사용자 id를 확인해주세요."));
    }


    /**
     * (뉴스&보도) 조회수 증가
     */
    @PatchMapping("/viewCnt/{id}")
    public ResponseEntity increaseView(@PathVariable(name = "id") Long id,
                                       @PathVariable(name = "lang", required = true) String lang,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        Optional<News> optionalNews = newsRepository.findById(id);
        if (!optionalNews.isPresent()) {
            log.error("status = {}, message = {}", "404", "일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
        }
        News news = optionalNews.get();

        newsRepository.increaseViewNews(news.getId());

        return ResponseEntity.ok(id + "번 뉴스&보도 조회수가 증가되었습니다.");
    }


    /**
     * (뉴스&보도) 이미지 출력
     */
    @GetMapping("/viewImage/{folderName}/{fileSaveName}")
    public void viewEditorImages(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String folderName,
            @PathVariable String fileSaveName) {
        AccessLogUtil.fileViewAccessLog(request);

        File serverFile = new File(fileDir + savePath + "/" + folderName + "/" +fileSaveName);
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




}
