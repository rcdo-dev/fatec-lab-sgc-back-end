package br.com.sgc.api.burial.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import br.com.sgc.api.burial.dto.request.ExhumationInspectionRequestDTO;
import br.com.sgc.api.burial.entity.BurialEntity;
import br.com.sgc.api.burial.entity.ExhumationInspectionEntity;
import br.com.sgc.api.burial.mapper.ExhumationInspectionMapper;
import br.com.sgc.api.burial.repositories.BurialRepository;
import br.com.sgc.api.burial.repositories.ExhumationInspectionRespository;
import br.com.sgc.api.common.enums.BurialStatus;
import br.com.sgc.api.common.enums.DecompositionStatus;
import br.com.sgc.api.common.exception.classes.BusinessException;

@ExtendWith(MockitoExtension.class)
public class ExhumationInspectionServiceTest {

    private BurialEntity burial;
    
    @Mock
    private ExhumationInspectionMapper mapper;
    
    @Mock
    private BurialRepository burialRepository;
    
    @Mock
    private ExhumationInspectionRespository respository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ExhumationInspectionService service;

    @BeforeEach
    void setUp() {
        this.burial = new BurialEntity();
        this.burial.setId(1L);
        this.burial.setDate(LocalDate.of(2023, 8, 7));
        this.burial.setTime(LocalTime.of(15, 30));
        this.burial.setStatus(BurialStatus.COMPLETED);

        this.service = new ExhumationInspectionService(burialRepository, respository, mapper, messageSource);
    }

    @Test
    void throwExceptionWhenPeriodIsNotRespected() {

         // 1. Datas da requisição
    LocalDate dataInspecaoInvalida = LocalDate.of(2026, 8, 6);
    LocalDate dataSubsequente = LocalDate.of(2028, 8, 6);

        var request = new ExhumationInspectionRequestDTO(
            dataInspecaoInvalida,
            DecompositionStatus.INCOMPLETE,
            dataSubsequente,
            "Mais exemplos de observações",
            burial.getId()
        );

        var exhumationInspection = new ExhumationInspectionEntity();
        exhumationInspection.setBurial(burial);
        exhumationInspection.setInspectedAt(dataInspecaoInvalida);

        // Cenário
       // when(burialRepository.findById(request.burialId())).thenReturn(Optional.of(burial));

        when(mapper.toEntity(request)).thenReturn(exhumationInspection);

        // Ação e Verificação
        assertThrows(BusinessException.class, () -> {
            service.save(request);
        });
    }

}
