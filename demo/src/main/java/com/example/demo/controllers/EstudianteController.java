package com.example.demo.controllers;

import ar.com.uade.tiendaOnline.tpo.entidad.Categoria;
import ar.com.uade.tiendaOnline.tpo.entidad.dto.CategoriaDTO;

import ar.com.uade.tiendaOnline.tpo.excepciones.CategoriaDuplicateExcepcion;
import ar.com.uade.tiendaOnline.tpo.excepciones.ProductoInexistenteExcepcion;
import ar.com.uade.tiendaOnline.tpo.servicios.categoria.CategoriaServicio;
import ar.com.uade.tiendaOnline.tpo.servicios.categoria.ICategoriaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("categorias")
public class EstudianteController {

        @Autowired
        private ICategoriaServicio categoriaServicio;

        @GetMapping
        public ResponseEntity<Page<Categoria>> getCategorias(
                @RequestParam(required = false) Integer page,
                @RequestParam(required = false) Integer size) {
            if (page == null || size == null)
                return ResponseEntity.ok(categoriaServicio.getCategorias(PageRequest.of(0, Integer.MAX_VALUE)));
            return ResponseEntity.ok(categoriaServicio.getCategorias(PageRequest.of(page, size)));
        }


        @GetMapping("/{categoriaId}")
        public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long categoriaId) {
            Optional<Categoria> result = categoriaServicio.getCategoriaById(categoriaId);
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());

        }


        @PostMapping
        public ResponseEntity<Object> crearCategoria(@RequestBody CategoriaDTO categoriaRequest)
                throws CategoriaDuplicateExcepcion {
            Categoria result = categoriaServicio.crearCategoria(categoriaRequest.getDescripcion());
            return ResponseEntity.created(URI.create("/categorias/" + result.getId())).body(result);
        }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        try {
            categoriaServicio.actualizarCategoria(id, categoriaDTO);
            return ResponseEntity.ok().build();
        } catch (ProductoInexistenteExcepcion e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }





    }




