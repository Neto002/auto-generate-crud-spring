package com.neto.services;

import com.neto.controllers.BookController;
import com.neto.data.vo.v1.BookVO;
import com.neto.exceptions.RequiredObjectIsNullException;
import com.neto.exceptions.ResourceNotFoundException;
import com.neto.mapper.DozerMapper;
import com.neto.model.Book;
import com.neto.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository repository;

    @Autowired
    PagedResourcesAssembler<BookVO> assembler;

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

        logger.info("Finding all Book!");

        Page<Book> BookPage = repository.findAll(pageable);

        Page<BookVO> BookVosPage = BookPage.map(o ->
                DozerMapper.parseObject(o, BookVO.class));
        BookVosPage.map(o ->
                o.add(linkTo(methodOn(BookController.class).findById(o.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(BookVosPage, link);
    }

    public BookVO findById(Long id) {

        logger.info("Finding one Book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO Book) {

        if (Book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Book!");
        var entity = DozerMapper.parseObject(Book, Book.class);
        var vo =  DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO Book) {

        if (Book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Book!");

        var entity = repository.findById(Book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(Book.getAuthor());
        entity.setLaunchDate(Book.getLaunchDate());
        entity.setPrice(Book.getPrice());
        entity.setTitle(Book.getTitle());

        var vo =  DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {

        logger.info("Deleting one Book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }
}
