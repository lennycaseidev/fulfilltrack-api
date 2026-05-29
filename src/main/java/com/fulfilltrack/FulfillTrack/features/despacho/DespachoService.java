package com.fulfilltrack.FulfillTrack.features.despacho;

import com.fulfilltrack.FulfillTrack.common.exception.EntidadDuplicadaException;
import com.fulfilltrack.FulfillTrack.common.exception.EntidadNoEncontradaException;
import com.fulfilltrack.FulfillTrack.common.exception.OperacionNoPermitidaException;
import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.despacho.mapper.DespachoMapper;
import com.fulfilltrack.FulfillTrack.features.pedido.EstadoPedido;
import com.fulfilltrack.FulfillTrack.features.pedido.IPedidoService;
import com.fulfilltrack.FulfillTrack.features.pedido.PedidoEntity;
import com.fulfilltrack.FulfillTrack.features.usuario.IUsuarioService;
import com.fulfilltrack.FulfillTrack.features.usuario.UsuarioEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DespachoService implements IDespachoService {

    private final DespachoRepository despachoRepository;
    private final DespachoMapper despachoMapper;
    private final IPedidoService pedidoService;
    private final IUsuarioService usuarioService;

    public DespachoService(DespachoRepository despachoRepository, DespachoMapper despachoMapper, IPedidoService pedidoService, IUsuarioService usuarioService) {
        this.despachoRepository = despachoRepository;
        this.despachoMapper = despachoMapper;
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @Override
    @Transactional
    public DespachoResponseDTO crearDespacho(DespachoRequestDTO request) {
        PedidoEntity pedido = pedidoService.obtenerPedidoUuid(request.getPedidoUuid());
        if (pedido.getEstado() != EstadoPedido.LISTO_PARA_DESPACHO) {
            throw new OperacionNoPermitidaException(
                    "el pedido debe estar en estado LISTO_PARA_DESPACHO para ser despachado. Estado actual: " + pedido.getEstado());
        }
        if (despachoRepository.existsByPedido_Uuid(request.getPedidoUuid())) {
            throw new EntidadDuplicadaException("el pedido ya tiene un despacho registrado");
        }
        UsuarioEntity usuario = usuarioService.obtenerUsuarioEntidad(request.getUsuarioUuid());
        DespachoEntity despacho = DespachoEntity.builder()
                .pedido(pedido)
                .usuario(usuario)
                .tracking("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .courier(request.getCourier())
                .build();
        DespachoEntity guardado = despachoRepository.save(despacho);
        pedidoService.marcarDespachado(pedido.getUuid());
        return despachoMapper.toResponseDTO(guardado);
    }

    @Override
    public DespachoResponseDTO obtenerDespachoPorUuid(UUID uuid) {
        return despachoMapper.toResponseDTO(obtenerDespachoEntidad(uuid));
    }

    @Override
    public DespachoResponseDTO obtenerDespachoPorPedido(UUID pedidoUuid) {
        return despachoMapper.toResponseDTO(
                despachoRepository.findByPedido_Uuid(pedidoUuid)
                        .orElseThrow(() -> new EntidadNoEncontradaException("no se encontró despacho para el pedido indicado")));
    }

    @Override
    public List<DespachoResponseDTO> listarDespachos() {
        return despachoMapper.toResponseList(despachoRepository.findAll());
    }

    private DespachoEntity obtenerDespachoEntidad(UUID uuid) {
        return despachoRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntidadNoEncontradaException("no se encontró el despacho"));
    }
}