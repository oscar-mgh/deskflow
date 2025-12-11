package com.github.oscarmgh.deskflow.services;

import java.util.List;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketCategoryRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketCategoryResponse;

public interface TicketCategoryService {

	TicketCategoryResponse createCategory(TicketCategoryRequest request);

	List<TicketCategoryResponse> getAllCategories();

	TicketCategoryResponse getCategoryById(Long id);

	TicketCategoryResponse getCategoryByName(String name);

	void deleteCategory(Long id);
}
