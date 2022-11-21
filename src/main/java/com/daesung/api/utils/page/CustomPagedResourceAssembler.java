//package com.daesung.api.utils.page;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
//import org.springframework.data.web.PagedResourcesAssembler;
//import org.springframework.hateoas.*;
//import org.springframework.hateoas.server.RepresentationModelAssembler;
//import org.springframework.util.Assert;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//import org.springframework.web.util.UriComponents;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import javax.annotation.Nullable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.springframework.web.util.UriComponentsBuilder.fromUri;
//
//public class CustomPagedResourceAssembler<T> extends PagedResourcesAssembler<T> {
//
//    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver;
//    private final Optional<UriComponents> baseUri;
//    private final int PAGE_SIZE;
//    private final int DEFAULT_PAGE_SIZE = 10;
//
//    public CustomPagedResourceAssembler(HateoasPageableHandlerMethodArgumentResolver pageableResolver, @Nullable Integer pageSize) {
//        super(pageableResolver,null);
//        this.pageableResolver = pageableResolver;
//        baseUri = Optional.empty();
//        this.PAGE_SIZE = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
//    }
//
//    @Override
//    public PagedModel<EntityModel<T>> toModel(Page<T> entity) {
//        return toModel(entity, EntityModel::of);
//    }
//
//    @Override
//    public <R extends RepresentationModel<?>> PagedModel<R> toModel(Page<T> page, RepresentationModelAssembler<T, R> assembler) {
//        Assert.notNull(page, "Page must not be null!");
//        Assert.notNull(assembler, "ResourceAssembler must not be null!");
//
//        List<R> resources = new ArrayList<>(page.getNumberOfElements());
//
//        for (T element : page) {
//            resources.add(assembler.toModel(element));
//        }
//
//        PagedModel<R> resource = createPagedModel(resources, asPageMetadata(page), page);
//        return addPaginationLinks(resource, page, Optional.empty());
//    }
//
//    private PagedModel.PageMetadata asPageMetadata(Page<?> page) {
//
//        Assert.notNull(page, "Page must not be null!");
//        int number = page.getNumber();
//        return new PagedModel.PageMetadata(page.getSize(), number, page.getTotalElements(), page.getTotalPages());
//    }
//
//    private <R> PagedModel<R> addPaginationLinks(PagedModel<R> resources, Page<?> page, Optional<Link> link) {
//
//        UriTemplate base = getUriTemplate(link);
//        boolean isNavigable = page.hasPrevious() || page.hasNext();
//
//        int currIndex = page.getNumber();
//        int lastIndex = page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1;
//        int baseIndex = (currIndex / this.PAGE_SIZE) * this.PAGE_SIZE;
//        int prevIndex = baseIndex - this.PAGE_SIZE;
//        int nextIndex = baseIndex + this.PAGE_SIZE;
//        int size = page.getSize();
//        Sort sort = page.getSort();
//
//        if(lastIndex < currIndex)
//            throw new IndexOutOfBoundsException("Page total size must not be less than page size");
//
//        if (isNavigable) {
//            resources.add(createLink(base, PageRequest.of(0, size, sort), IanaLinkRelations.FIRST));
//        }
//
//        Link selfLink = link.map(it -> it.withSelfRel()).orElseGet(() -> createLink(base, page.getPageable(), IanaLinkRelations.SELF));
//
//        resources.add(selfLink);
//
//        if (prevIndex >= 0) {
//            resources.add(createLink(base,
//                    PageRequest.of(prevIndex, size, sort),
//                    IanaLinkRelations.PREV));
//        }
//
//        for(int i = 0; i < this.PAGE_SIZE; i++){
//            if(baseIndex + i <= lastIndex){
//                resources.add(createLink(base, PageRequest.of(baseIndex + i, size, sort), LinkRelation.of("pagination")));
//            }
//        }
//
//        if(nextIndex < lastIndex){
//            resources.add(createLink(base, PageRequest.of(nextIndex, size, sort), IanaLinkRelations.NEXT));
//        }
//
//        if (isNavigable) {
//            resources.add(createLink(base, PageRequest.of(lastIndex, size, sort), IanaLinkRelations.LAST));
//        }
//
//        return resources;
//    }
//
//    private UriTemplate getUriTemplate(Optional<Link> baseLink) {
//        return UriTemplate.of(baseLink.map(Link::getHref).orElseGet(this::baseUriOrCurrentRequest));
//    }
//
//    private String baseUriOrCurrentRequest() {
//        return baseUri.map(Object::toString).orElseGet(CustomPagedResourceAssembler::currentRequest);
//    }
//
//    private static String currentRequest() {
//        return ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
//    }
//
//    private Link createLink(UriTemplate base, Pageable pageable, LinkRelation relation) {
//        UriComponentsBuilder builder = fromUri(base.expand());
//        pageableResolver.enhance(builder, getMethodParameter(), pageable);
//        return Link.of(UriTemplate.of(builder.build().toString()), relation);
//    }
//
//
//
//}
