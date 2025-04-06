package com.example.techchallenge.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelMapperBase {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private ModelMapperBase() {
        // Classe utilitária, não deve ser instanciada.
    }

    public static <S, D> D map(S source, Class<D> destinationType) {
        if(source == null){
            return null;
        }

        return modelMapper.map(source, destinationType);
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return new ArrayList<>();
        }

        return sourceList.stream()
                .map(item -> modelMapper.map(item, targetClass))
                .toList();
    }

    public static <T> Set<T> mapCollection(Collection<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return new HashSet<>();
        }

        return sourceList.stream()
                .map(item -> modelMapper.map(item, targetClass))
                .collect(Collectors.toSet());
    }

    public static <D, T> Page<D> mapPage(Page<T> sourcePage, Class<D> destinationType) {
        if (sourcePage == null) {
            return new PageImpl<>(Collections.emptyList());
        }

        List<D> dtoList = mapList(sourcePage.getContent(), destinationType);
        return new PageImpl<>(dtoList, sourcePage.getPageable(), sourcePage.getTotalElements());
    }

    public static <T> Page<T> listToPage(List<T> source, Integer page, Integer size){
        int pNumber = page != null ? page : 0;
        int pSize = size != null ? size : 10;

        int start = Math.min(pNumber * pSize, source.size());
        int end = Math.min(start + pSize, source.size());

        return new PageImpl<>(source.subList(start, end), PageRequest.of(pNumber, pSize), source.size());
    }
}
