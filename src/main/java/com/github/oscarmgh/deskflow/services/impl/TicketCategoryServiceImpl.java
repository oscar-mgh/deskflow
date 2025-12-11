package com.github.oscarmgh.deskflow.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketCategoryRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketCategoryResponse;
import com.github.oscarmgh.deskflow.entities.TicketCategory;
import com.github.oscarmgh.deskflow.exceptions.tickets.CategoryNotFoundException;
import com.github.oscarmgh.deskflow.repositories.TicketCategoryRepository;
import com.github.oscarmgh.deskflow.services.TicketCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketCategoryServiceImpl implements TicketCategoryService {

    private final TicketCategoryRepository categoryRepository;

    @Override
    public TicketCategoryResponse createCategory(TicketCategoryRequest request) {
        String name = request.getName().trim();
        if (name.length() < 2 || name.length() > 30) {
            throw new IllegalArgumentException("Category name must be between 2 and 30 characters");
        }

        categoryRepository.findByName(name).ifPresent(c -> {
            throw new IllegalStateException("Category already exists");
        });

        TicketCategory category = TicketCategory.builder()
                .name(name)
                .build();

        TicketCategory saved = categoryRepository.save(category);
        return new TicketCategoryResponse(saved);
    }

    @Override
    public List<TicketCategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(TicketCategoryResponse::new)
                .toList();
    }

    @Override
    public TicketCategoryResponse getCategoryById(Long id) {
        TicketCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id.toString()));
        return new TicketCategoryResponse(category);
    }

    @Override
    public TicketCategoryResponse getCategoryByName(String name) {
        TicketCategory category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return new TicketCategoryResponse(category);
    }

    @Override
    public void deleteCategory(Long id) {
        TicketCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id.toString()));
        categoryRepository.delete(category);
    }
}
