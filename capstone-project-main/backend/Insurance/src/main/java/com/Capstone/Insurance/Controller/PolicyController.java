package com.Capstone.Insurance.Controller;


import com.Capstone.Insurance.Entity.Policy;
import com.Capstone.Insurance.Exceptions.PolicyAlreadyExistsException;
import com.Capstone.Insurance.Repository.PolicyRepository;
import com.Capstone.Insurance.Service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/insurances")
public class PolicyController {
    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public Policy createPolicy(@RequestBody Policy policy) {
        return policyService.createPolicy(policy);
    }

    @GetMapping("/{policynumber}")
    public Policy getPolicyBypolicynumber(@PathVariable String policynumber) {
        return policyService.getPolicyBypolicynumber(policynumber);

    }


    @GetMapping
    public List<Policy> getAllPolicies() {
        return policyService.getAllPolicies();
    }
    @PostMapping("/link-account")
    public Policy linkAccount(@RequestBody Policy policy) throws PolicyAlreadyExistsException {
        Policy existingPolicy = policyService.getPolicyBypolicynumber(policy.getpolicynumber());
        if (existingPolicy != null) {
            throw new PolicyAlreadyExistsException("Policy with number " + policy.getpolicynumber() + " already exists.");
        }
        return policyService.createPolicy(policy);
    }

}

