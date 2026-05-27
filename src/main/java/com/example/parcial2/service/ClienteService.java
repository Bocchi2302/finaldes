package com.example.parcial2.service;

import com.example.parcial2.dto.request.ClienteRequest;
import com.example.parcial2.dto.response.ClienteResponse;
import com.example.parcial2.entity.Cliente;
import com.example.parcial2.exception.DuplicateResourceException;
import com.example.parcial2.exception.ResourceNotFoundException;
import com.example.parcial2.mapper.ClienteMapper;
import com.example.parcial2.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        validarDocumentoDuplicado(request.documento(), null);
        return clienteMapper.toResponse(clienteRepository.save(clienteMapper.toEntity(request)));
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = obtenerEntidad(id);
        validarDocumentoDuplicado(request.documento(), id);
        clienteMapper.update(cliente, request);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void desactivar(Long id) {
        Cliente cliente = obtenerEntidad(id);
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    public ClienteResponse obtener(Long id) {
        return clienteMapper.toResponse(obtenerEntidad(id));
    }

    public Cliente obtenerEntidad(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + id));
    }

    public List<ClienteResponse> listar() {
        return clienteRepository.findAllByOrderByNombreAsc().stream().map(clienteMapper::toResponse).toList();
    }

    private void validarDocumentoDuplicado(String documento, Long excludeId) {
        if (documento == null || documento.isBlank()) {
            return;
        }
        clienteRepository.findByDocumento(documento).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new DuplicateResourceException("Ya existe un cliente con ese documento");
            }
        });
    }
}
