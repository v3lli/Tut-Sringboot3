package com.velli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {

    public final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
    @GetMapping("/")
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    record NewCustomerRequest (
            String name,
            String email,
            Integer age
    ){}

    record CustomerRequest(
            Integer id,
            String name,
            String email,
            Integer age
    ){}
    @PostMapping("/")
    public void addCustomer(@RequestBody NewCustomerRequest request){
        Customer customer = new Customer();
        customer.setAge(request.age);
        customer.setEmail(request.email);
        customer.setName(request.name);

        customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id){
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public HttpEntity<String> updateCustomer(@PathVariable("customerId") Integer id, @RequestBody CustomerRequest request){
        Customer customer = new Customer();
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(666)).body("Customer not found");
        }
        else{
            customer = optionalCustomer.get();
            customer.setName(request.name);
            customer.setAge(request.age);
            customer.setEmail(request.email);
            customerRepository.save(customer);
            return ResponseEntity.ok("Customer updated successfully");
        }
    }
}
