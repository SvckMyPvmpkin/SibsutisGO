package com.sibsutisgo.controller;

import com.sibsutisgo.dto.SupportTicketByStatusDTO;
import com.sibsutisgo.dto.SupportTicketCreateDTO;
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
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicketCreateDTO request){
        SupportTicket savedTicket = supportService.createTicket(request.getMessage());
        return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getTicketByID(@PathVariable Long id){
        return supportService.getTicketById(id)
                .map(ticket -> new ResponseEntity<>(ticket, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/active")
    public ResponseEntity<SupportTicketByStatusDTO> getTicketByActiveStatus(){
        return new ResponseEntity<>(supportService.getActiveTickets(), HttpStatus.OK);
    }

    @GetMapping("/closed")
    public ResponseEntity<SupportTicketByStatusDTO> getTicketByClosedStatus(){
        return new ResponseEntity<>(supportService.getClosedTickets(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SupportTicket> patchTicket(@PathVariable Long id){
        return new ResponseEntity<>(supportService.closeTicket(id), HttpStatus.OK);
    }
}
