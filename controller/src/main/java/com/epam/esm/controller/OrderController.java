package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static com.epam.esm.util.PaginationUtil.PAGE_NUMBER_DEFAULT_TEXT;
import static com.epam.esm.util.PaginationUtil.PAGE_SIZE_DEFAULT_TEXT;


/**
 * Controller for Order entity.
 */
@RequestMapping("/orders")
@SecurityRequirement(name = "oauth2")
@Validated
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    private final OrderService orderService;
    private final OrderAssembler orderAssembler;

    @Autowired
    public OrderController(OrderService orderService, OrderAssembler orderAssembler) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
    }

    /**
     * Retrieves all Orders for page
     *
     * @param size                    the page size
     * @param page                    the page number
     * @param userId                  the user id
     * @param pagedResourcesAssembler the paged resources assembler
     * @return the paged entity model of found Orders
     */
    @GetMapping
    public PagedModel<EntityModel<OrderDto>> all(
            @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_TEXT) @Positive int size,
            @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_TEXT) @Positive int page,
            @RequestParam(required = false) @Positive Long userId,
            PagedResourcesAssembler<OrderDto> pagedResourcesAssembler) {
        Page<OrderDto> resultPage =
                (userId == null)
                        ? orderService.findAll(PageRequest.of(page - 1, size))
                        : orderService.findByUserId(userId, PageRequest.of(page - 1, size));
        return pagedResourcesAssembler.toModel(resultPage, orderAssembler);
    }


    /**
     * Retrieves existing Order by its id.
     *
     * @param id the Order id
     * @return entity model of found Order
     */
    @GetMapping("{id}")
    public EntityModel<OrderDto> one(@PathVariable @Positive Long id) {
        return orderAssembler.toModel(orderService.findById(id));
    }


    /**
     * Create new Order
     *
     * @param newOrder the new Order
     * @return entity model of created found Order
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<OrderDto> create(@Valid @RequestBody OrderDto newOrder) {
        return orderAssembler.toModel(orderService.save(newOrder));
    }
}
