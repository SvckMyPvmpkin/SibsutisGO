package com.sibsutisgo.controller;

import com.sibsutisgo.dto.SupportTicketByStatusResponse;
import com.sibsutisgo.dto.SupportTicketCreateRequest;
import com.sibsutisgo.dto.SupportTicketResponse;
import com.sibsutisgo.dto.SupportTicketStatusChangeRequest;
import com.sibsutisgo.model.SupportStatus;
import com.sibsutisgo.model.SupportTicket;
import com.sibsutisgo.service.SupportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/support")
public class SupportController {
    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @PostMapping
    public ResponseEntity<SupportTicketResponse> createTicket(@RequestBody SupportTicketCreateRequest request){
        SupportTicketResponse savedTicket = supportService.createTicket(request.message());
        return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponse> getTicketByID(@PathVariable Long id){
        return supportService.getTicketById(id)
                .map(ticket -> new ResponseEntity<>(ticket, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/active")
    public ResponseEntity<SupportTicketByStatusResponse> getTicketByActiveStatus(){
        return new ResponseEntity<>(supportService.getActiveTickets(), HttpStatus.OK);
    }

    @GetMapping("/closed")
    public ResponseEntity<SupportTicketByStatusResponse> getTicketByClosedStatus(){
        return new ResponseEntity<>(supportService.getClosedTickets(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SupportTicketResponse> patchTicket(@PathVariable Long id, @RequestBody SupportTicketStatusChangeRequest request) {
        return new ResponseEntity<>(supportService.changeStatusTicket(id, request.status()), HttpStatus.OK);
    }
}
