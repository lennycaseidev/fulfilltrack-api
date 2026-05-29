package com.fulfilltrack.FulfillTrack.features.despacho;

import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.despacho.dto.DespachoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.despacho.mapper.DespachoMapper;
import com.fulfilltrack.FulfillTrack.features.pedido.IPedidoService;
import com.fulfilltrack.FulfillTrack.features.usuario.IUsuarioService;
import org.springframework.stereotype.Service;

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
    public DespachoResponseDTO crearDespacho(DespachoRequestDTO request) { return null; }

    @Override
    public DespachoResponseDTO obtenerDespachoPorUuid(UUID uuid) { return null; }

    @Override
    public DespachoResponseDTO obtenerDespachoPorPedido(UUID pedidoUuid) { return null; }

    @Override
    public List<DespachoResponseDTO> listarDespachos() { return null; }

    @Override
    public DespachoResponseDTO marcarEntregado(UUID uuid) { return null; }
}