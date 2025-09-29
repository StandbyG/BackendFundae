package com.fundae.backend.dto;

import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Usuario;

public class AjusteRazonableMapper {

    public static AjusteRazonableResponseDTO toResponse(AjusteRazonable a) {
        if (a == null) return null;

        AjusteRazonableResponseDTO dto = new AjusteRazonableResponseDTO();
        dto.setIdAjuste(a.getIdAjuste());
        dto.setEstado(a.getEstado());
        dto.setFechaRecomendacion(a.getFechaRecomendacion());
        dto.setFechaImplementacion(a.getFechaImplementacion());
        dto.setAlertado(a.isAlertado());
        dto.setOrigen(a.getOrigen() != null ? a.getOrigen().name() : null);
        dto.setAjustesSugeridos(a.getAjustesSugeridos());
        dto.setEspacio(a.getEspacio());
        dto.setRefNormativa(a.getRefNormativa());
        dto.setRefFotografica(a.getRefFotografica());
        dto.setDificultad(String.valueOf(a.getDificultad()));
        dto.setUrgencia(String.valueOf(a.getUrgencia()));
        dto.setTipoAjuste(coalesce(a.getTipoAjuste(), inferirTipoDesde(a.getAjustesSugeridos(), a.getEspacio())));
        dto.setDescripcion(coalesce(a.getDescripcion(), a.getAjustesSugeridos()));

        Usuario u = a.getUsuario();
        if (u != null) {
            UsuarioDTO ur = new UsuarioDTO();
            ur.setIdUsuario(u.getIdUsuario());
            ur.setNombre(u.getNombre());
            ur.setCorreo(u.getCorreo());
            ur.setNombreEmpresa(u.getNombreEmpresa());
            dto.setUsuario(ur);
        }

        return dto;
    }

    private static String coalesce(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static String inferirTipoDesde(String ajustesSugeridos, String espacio) {
        if (espacio != null && !espacio.isBlank()) return "AJUSTE_EN_" + espacio.toUpperCase();
        if (ajustesSugeridos != null && !ajustesSugeridos.isBlank()) return "AJUSTE_ONG";
        return null;
    }
}
