package com.Capstone.Insurance.Service;

import com.Capstone.Insurance.Entity.Policy;
import com.Capstone.Insurance.Exceptions.PolicyAlreadyExistsException;
import com.Capstone.Insurance.Repository.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class PolicyService {
    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    public Policy getPolicyBypolicynumber(String policynumber) {
        return policyRepository.findBypolicynumber(policynumber);
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }
    public Policy linkAccount(@RequestBody Policy policy) throws PolicyAlreadyExistsException {
        Policy existingPolicy = policyRepository.findBypolicynumber(policy.getpolicynumber());
        if (existingPolicy != null) {
            throw new PolicyAlreadyExistsException("Policy with number " + policy.getpolicynumber() + " already exists.");
        }

        return policyRepository.save(policy);
    }

}

