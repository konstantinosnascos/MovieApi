package com.learn2earn.movie_api.controller;

import com.learn2earn.movie_api.dto.LoanResponseDTO;
import com.learn2earn.movie_api.dto.ReturnLoanDTO;
import com.learn2earn.movie_api.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

//    @GetMapping
//    public List<LoanResponseDTO> getAllLoans(){
//        return loanService.getAllLoans();
//    }

    @PostMapping("/{movieId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void loanMovie(@PathVariable Long movieId, @RequestParam String borrowerName){
        loanService.loanMovie(movieId, borrowerName);
    }

    @PutMapping("/{loanId}/return")
    public LoanResponseDTO returnLoan(@PathVariable Long loanId){
        return loanService.returnMovie(loanId);
    }

    @GetMapping("/{movieId}/active")
    public ReturnLoanDTO getActiveLoan(@PathVariable Long movieId) {
        return loanService.getActiveLoan(movieId);
    }

}
