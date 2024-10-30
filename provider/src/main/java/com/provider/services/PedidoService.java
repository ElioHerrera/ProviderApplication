package com.provider.services;

import com.provider.converter.ItemConverter;
import com.provider.converter.PedidoConverter;
import com.provider.dto.ItemDTO;
import com.provider.dto.PedidoDTO;
import com.provider.entities.*;
import com.provider.other.Consola;
import com.provider.repositories.ItemRepository;
import com.provider.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoService productoService;

    @Transactional
    public PedidoDTO crearPedido(PedidoDTO pedidoDTO) {
        try {
            Comercio comercio = obtenerComercioDelPediodo(pedidoDTO);
            Empresa empresa = obtenerEmpresaDelPedido(pedidoDTO);
            imprimirPedidoEnConsola(pedidoDTO);

            // Obtener el número máximo de pedidos para este cliente y proveedor
            int numeroPedido = pedidoRepository.findMaxNumeroPedidoByClienteAndProveedor(comercio.getId(), empresa.getId()) + 1;

            System.out.println("Número de pedido calculado: " + numeroPedido);

            Pedido pedido = PedidoConverter.dtoToEntity(pedidoDTO); // Convierte DTO a entidad
            pedido.setNumeroPedido(numeroPedido); // Asigna el nuevo número de pedido
            pedido.setEstado(EstadoPedido.PENDIENTE); // Establece el estado inicial si es necesario
            pedido.setCliente(comercio);
            pedido.setProveedor(empresa);

            // Guarda el pedido
            Pedido nuevoPedido = pedidoRepository.save(pedido);

            System.out.println("Pedido guardado: " + nuevoPedido.getId());

            // Guarda los items del pedido
            for (ItemDTO itemDTO : pedidoDTO.getItems()) {

                Long idProducto = itemDTO.getProducto().getIdProducto();

                Item item = ItemConverter.dtoToEntity(itemDTO, nuevoPedido); // Asigna el pedido a cada item
                // Verifica si el producto existe en la base de datos antes de asignarlo
                Optional<Producto> productoOptional = productoService.obtenerProductoOpcionalPorId(idProducto);

                Producto producto = productoOptional.orElseThrow(() ->
                        new NoSuchElementException("Producto con ID " + item.getProducto().getId() + " no encontrado")
                );

                item.setProducto(producto); // Asocia el producto verificado al item

                itemRepository.save(item); // Guarda el item en la base de datos
                System.out.println("Item guardado: " + item.getId());
            }

            //enviarNotificacionWhatsApp(pedidoDTO, empresa.getTelefono());

            // Retorna el pedido creado como DTO
            return PedidoConverter.entityToDTO(nuevoPedido);
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
            e.printStackTrace(); // Imprime la traza de la excepción
            return null; // Maneja el error según tus necesidades
        }
    }

    private Comercio obtenerComercioDelPediodo(PedidoDTO pedidoDTO) {

        Long clienteId = pedidoDTO.getClienteId();
        Usuario usuarioCliente = usuarioService.obtenerUsuarioPorId(clienteId);

        Comercio comercio = usuarioCliente.getPerfil().getComercio();
        System.out.println(Consola.textoRojo() + "Client Id: " + usuarioCliente.getId() + " Nombre: " + usuarioCliente.getPerfil().getNombre() + " Empresa Id: " + comercio.getId() + " Nombre: " + comercio.getNombre() + Consola.finTexto());

        return comercio;
    }

    private Empresa obtenerEmpresaDelPedido(PedidoDTO pedidoDTO) {

        Long proveedorId = pedidoDTO.getProveedorId();
        Usuario usuarioProveedor = usuarioService.obtenerUsuarioPorId(proveedorId);

        Empresa empresa = usuarioProveedor.getPerfil().getEmpresa();
        System.out.println(Consola.textoRojo() + "Proveedor Id: " + usuarioProveedor.getId() + " Nombre: " + usuarioProveedor.getPerfil().getNombre() + " Empresa Id: " + empresa.getId() + " Nombre: " + empresa.getNombre() + Consola.finTexto());

        return empresa;
    }

    private void imprimirPedidoEnConsola(PedidoDTO pedidoDTO) {
        System.out.println(Consola.textoAmarillo() + "----------------------------------------------\n" +
                "PEDIDO -> Fecha: " + pedidoDTO.getFecha() + "\n" +
                "Id Cliente: " + pedidoDTO.getClienteId() + "    Id Proveedor: " + pedidoDTO.getProveedorId() + "\n" +
                "----------------------------------------------\n" + "Detalle:");
        for (ItemDTO item : pedidoDTO.getItems()) {
            System.out.println(item.getCantidad() + " - " + item.getProducto().getNombre() + "   pr. $ " + item.getPrecioUnitario() + "     sub $ " + item.getSubtotal());
        }

        System.out.println("         SubTotal: $ " + pedidoDTO.getSubtotalPedido() + "\n" +
                "            TOTAL: $ " + pedidoDTO.getTotal() + "\n" +
                "-------------------------------------------" + Consola.finTexto());

    }

    public void enviarNotificacionWhatsApp(PedidoDTO pedidoDTO, String telefonoProveedor) {
        String url = "https://graph.facebook.com/v13.0/{whatsapp_business_phone_number_id}/messages";  // URL de la API de WhatsApp
        String token = "your_access_token";  // Tu token de acceso de la API de WhatsApp Business

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        String mensaje = construirMensajePedido(pedidoDTO);

        // Cuerpo de la solicitud en formato JSON
        String jsonBody = "{ \"messaging_product\": \"whatsapp\", \"to\": \"" + telefonoProveedor + "\", \"type\": \"text\", \"text\": { \"body\": \"" + mensaje + "\" } }";

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForObject(url, request, String.class);
            System.out.println("Notificación de pedido enviada por WhatsApp");
        } catch (Exception e) {
            System.err.println("Error al enviar notificación de WhatsApp: " + e.getMessage());
        }
    }

    private String construirMensajePedido(PedidoDTO pedidoDTO) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Nuevo Pedido Recibido!\n\n");
        mensaje.append("Fecha: ").append(pedidoDTO.getFecha()).append("\n");
        mensaje.append("Id Cliente: ").append(pedidoDTO.getClienteId()).append("\n\n");
        mensaje.append("Detalle del Pedido:\n");

        for (ItemDTO item : pedidoDTO.getItems()) {
            mensaje.append("- ").append(item.getCantidad()).append(" x ")
                    .append(item.getProducto().getNombre()).append(" $")
                    .append(item.getPrecioUnitario()).append("\n");
        }
        mensaje.append("\nTotal: $").append(pedidoDTO.getTotal());
        return mensaje.toString();
    }


}