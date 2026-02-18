package com.example.demo.service;

import com.example.demo.model.EquivalenciaCalificacion;
import com.example.demo.model.EquivalenciaCalificacion.PaisEquivalencia;
import com.example.demo.repository.mongo.EquivalenciaCalificacionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class EquivalenciaCalificacionService {

    @Autowired
    private EquivalenciaCalificacionMONGORepository equivalenciaRepository;

    public EquivalenciaCalificacion crearLegislacion(EquivalenciaCalificacion legislacion) {
        if (legislacion == null) {
            throw new IllegalArgumentException("La legislacion es obligatoria.");
        }
        legislacion.setPaises(normalizarPaises(legislacion.getPaises()));
        return equivalenciaRepository.save(legislacion);
    }

    public EquivalenciaCalificacion actualizarLegislacion(String id, EquivalenciaCalificacion legislacion) {
        Optional<EquivalenciaCalificacion> existente = equivalenciaRepository.findById(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("No existe legislacion con id: " + id);
        }
        if (legislacion == null) {
            throw new IllegalArgumentException("La legislacion es obligatoria.");
        }

        legislacion.setId(id);
        legislacion.setPaises(normalizarPaises(legislacion.getPaises()));
        return equivalenciaRepository.save(legislacion);
    }

    public void eliminarLegislacion(String id) {
        if (!equivalenciaRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe legislacion con id: " + id);
        }
        equivalenciaRepository.deleteById(id);
    }

    public EquivalenciaCalificacion crearDocumentoMapeoGlobal(String versionGlobal,
                                                              LocalDate vigenciaDesde,
                                                              LocalDate vigenciaHasta,
                                                              Map<String, PaisEquivalencia> paises) {
        EquivalenciaCalificacion documento = new EquivalenciaCalificacion();
        documento.setVersionGlobal(versionGlobal);
        documento.setVigenciaDesde(vigenciaDesde);
        documento.setVigenciaHasta(vigenciaHasta);
        documento.setPaises(normalizarPaises(paises));

        return equivalenciaRepository.save(documento);
    }

    public PaisEquivalencia obtenerMapeoVigente(String paisCodigo, LocalDate fechaReferencia) {
        String paisNormalizado = normalizarPais(paisCodigo);
        LocalDate fecha = fechaReferencia != null ? fechaReferencia : LocalDate.now();

        List<EquivalenciaCalificacion> versiones = equivalenciaRepository.findAllByOrderByVigenciaDesdeDesc();
        for (EquivalenciaCalificacion version : versiones) {
            if (version.getVigenciaDesde() == null) {
                continue;
            }
            boolean inicia = !fecha.isBefore(version.getVigenciaDesde());
            boolean noVence = version.getVigenciaHasta() == null || !fecha.isAfter(version.getVigenciaHasta());
            if (!inicia || !noVence) {
                continue;
            }

            Map<String, PaisEquivalencia> paises = version.getPaises() != null ? version.getPaises() : Collections.emptyMap();
            PaisEquivalencia mapeoPais = paises.get(paisNormalizado);
            if (mapeoPais != null) {
                return mapeoPais;
            }
        }

        throw new IllegalArgumentException("No hay mapeo vigente para pais " + paisNormalizado + " en fecha " + fecha);
    }

    public Double convertirANotaSudafrica(String paisCodigo, String notaOrigen, LocalDate fechaReferencia) {
        if (notaOrigen == null || notaOrigen.isBlank()) {
            return null;
        }

        PaisEquivalencia mapeo = obtenerMapeoVigente(paisCodigo, fechaReferencia);
        Map<String, Double> equivalencias = mapeo.getEquivalencias() != null ? mapeo.getEquivalencias() : Collections.emptyMap();

        String notaNormalizada = notaOrigen.trim().toUpperCase(Locale.ROOT);
        if (equivalencias.containsKey(notaNormalizada)) {
            return equivalencias.get(notaNormalizada);
        }

        Double notaNumerica = parseDoubleSeguro(notaNormalizada);
        if (notaNumerica == null) {
            return equivalencias.getOrDefault("F", null);
        }

        List<Double> umbrales = new ArrayList<>();
        Map<Double, Double> valorPorUmbral = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : equivalencias.entrySet()) {
            Double umbral = parseDoubleSeguro(entry.getKey());
            if (umbral != null) {
                umbrales.add(umbral);
                valorPorUmbral.put(umbral, entry.getValue());
            }
        }

        umbrales.sort(Collections.reverseOrder());
        for (Double umbral : umbrales) {
            if (notaNumerica >= umbral) {
                return valorPorUmbral.get(umbral);
            }
        }

        if (mapeo.getUmbralMenorQue() != null && mapeo.getValorMenorQue() != null && notaNumerica < mapeo.getUmbralMenorQue()) {
            return mapeo.getValorMenorQue();
        }

        return null;
    }

    private Double parseDoubleSeguro(String valor) {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String normalizarPais(String paisCodigo) {
        if (paisCodigo == null) {
            return "";
        }
        String p = paisCodigo.trim().toUpperCase(Locale.ROOT);
        return switch (p) {
            case "ARGENTINA", "AR" -> "AR";
            case "USA", "US", "ESTADOS UNIDOS" -> "USA";
            case "UK", "INGLATERRA", "UNITED KINGDOM" -> "UK";
            case "ALEMANIA", "GERMANY", "DE" -> "DE";
            default -> p;
        };
    }

    private Map<String, PaisEquivalencia> normalizarPaises(Map<String, PaisEquivalencia> paises) {
        Map<String, PaisEquivalencia> normalizados = new LinkedHashMap<>();
        if (paises == null) {
            return normalizados;
        }
        for (Map.Entry<String, PaisEquivalencia> entry : paises.entrySet()) {
            normalizados.put(normalizarPais(entry.getKey()), entry.getValue());
        }
        return normalizados;
    }
}
