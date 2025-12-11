package com.github.oscarmgh.deskflow.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<TicketCategoryResponse> createCategory(@RequestBody TicketCategoryRequest request) {
		return ResponseEntity.ok(categoryService.createCategory(request));
	}

	@GetMapping
	public ResponseEntity<List<TicketCategoryResponse>> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	@GetMapping("/{id}")
	public ResponseEntity<TicketCategoryResponse> getCategoryById(@PathVariable Long id) {
		return ResponseEntity.ok(categoryService.getCategoryById(id));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<TicketCategoryResponse> getCategoryByName(@PathVariable String name) {
		return ResponseEntity.ok(categoryService.getCategoryByName(name));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}
}
