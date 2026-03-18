package br.com.sgc.api.cemitery.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import br.com.sgc.api.cemitery.entity.CemeteryEntity;
import br.com.sgc.api.cemitery.repositories.CemeteryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CemeteryService {
    private final CemeteryRepository cemeteryRepository;
    private final MessageSource messageSource;

    public CemeteryEntity save(CemeteryEntity entity){

        if(entity == null){
            throw new RuntimeException();
        }


        return cemeteryRepository.save(entity);
    }
}
