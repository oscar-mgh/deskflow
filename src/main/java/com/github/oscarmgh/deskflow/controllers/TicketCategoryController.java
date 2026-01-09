package com.github.oscarmgh.deskflow.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketCategoryRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketCategoryResponse;
import com.github.oscarmgh.deskflow.services.TicketCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class TicketCategoryController {

	private final TicketCategoryService categoryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TicketCategoryResponse createCategory(@RequestBody TicketCategoryRequest request) {
		return categoryService.createCategory(request);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<TicketCategoryResponse> getAllCategories() {
		return categoryService.getAllCategories();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public TicketCategoryResponse getCategoryById(@PathVariable Long id) {
		return categoryService.getCategoryById(id);
	}

	@GetMapping("/name/{name}")
	@ResponseStatus(HttpStatus.OK)
	public TicketCategoryResponse getCategoryByName(@PathVariable String name) {
		return categoryService.getCategoryByName(name);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
	}
}
