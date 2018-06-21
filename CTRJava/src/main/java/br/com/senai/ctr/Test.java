package br.com.senai.ctr;

import br.com.senai.ctr.dal.*;
import br.com.senai.ctr.model.*;
import com.google.firebase.database.*;

import java.util.*;

public class Test {

    static final HashMap<String, Usuario> usuarios = new HashMap<>();
    static final HashMap<String, Grupo> grupos = new HashMap<>();

    static final HashMap<String, Marca> marcas = new HashMap<>();
    static final HashMap<String, Equipamento> equipamentos = new HashMap<>();

    static final HashMap<String, Emissor> emissores = new HashMap<>();
    static final HashMap<String, Transmissao> transmissoes = new HashMap<>();

    public static void main(String[] args) {
        Firebase.inicializarFirebase();

        criarBase();

        GrupoDAO grpdao = new GrupoDAO(FirebaseDatabase.getInstance().getReference());

        for (String k : grupos.keySet()) {
            grpdao.syncCreate(grupos.get(k));
        }

        UsuarioDAO usrdao = new UsuarioDAO(FirebaseDatabase.getInstance().getReference());

        for (String k : usuarios.keySet()) {
            usrdao.syncCreate(usuarios.get(k));
        }

        MarcaDAO mardao = new MarcaDAO(FirebaseDatabase.getInstance().getReference());

        for (String k : marcas.keySet()) {
            mardao.syncCreate(marcas.get(k));
        }

        EmissorDAO emissdao = new EmissorDAO(FirebaseDatabase.getInstance().getReference());

        for (String k : emissores.keySet()) {
            emissdao.syncCreate(emissores.get(k));
        }

        EquipamentoDAO equidao = new EquipamentoDAO(FirebaseDatabase.getInstance().getReference());

        for (String k : equipamentos.keySet()) {
            equidao.syncCreate(equipamentos.get(k));
        }

        TransmissaoDAO tradao = new TransmissaoDAO(FirebaseDatabase.getInstance().getReference());

        for (String k : transmissoes.keySet()) {
            tradao.syncCreate(transmissoes.get(k));
        }

    }


    private static void criarUsuario() {
        Usuario usr = new Usuario()
                .setId("fa89sd8f09s0df")
                .setNome("Eduardo Macedo")
                .setEmail("dudmacedo@gmail.com")
                .setTipo(EnumTipoUsuario.ADMINISTRADOR);
        usuarios.put(usr.getId(), usr);

        usr = new Usuario()
                .setId("987f8s9df0s90")
                .setNome("Yanna Jaia")
                .setEmail("yannajaia@gmail.com")
                .setTipo(EnumTipoUsuario.OPERADOR);
        usuarios.put(usr.getId(), usr);

        usr = new Usuario()
                .setId("p234pio23p")
                .setNome("Marcos")
                .setEmail("marcos@gmail.com")
                .setTipo(EnumTipoUsuario.OPERADOR);
        usuarios.put(usr.getId(), usr);
    }

    private static void criarGrupos() {
        Grupo grpusu = new Grupo()
                .setNome("Sala PR-CPR");
        grupos.put("a", grpusu);

        grpusu = new Grupo()
                .setNome("Laboratório 104");
        grupos.put("b", grpusu);
    }

    private static void relacionaUsuarioGrupo() {
        grupos.get("a")
                .setUsuarios(
                        Arrays.asList(
                                usuarios.get("fa89sd8f09s0df")
                        )
                );
        grupos.get("b")
                .setUsuarios(
                        Arrays.asList(
                                usuarios.get("fa89sd8f09s0df"),
                                usuarios.get("987f8s9df0s90")
                        )
                );

        usuarios.get("fa89sd8f09s0df")
                .setGrupos(
                        Arrays.asList(
                                grupos.get("a"),
                                grupos.get("b")
                        )
                );
        usuarios.get("987f8s9df0s90")
                .setGrupos(
                        Arrays.asList(
                                grupos.get("b")
                        )
                );
    }

    private static void criarMarcas() {
        Marca mar = new Marca()
                .setId("elgin")
                .setNome("Elgin")
                .setModelos(new HashMap<>());
        marcas.put(mar.getId(), mar);

        Modelo mod = new Modelo()
                .setMarca(mar)
                .setId("SRFI-12000-2")
                .setNome("Ar Condicionado Split SRFI-12000-2")
                .setTipo(EnumTipoEquipamento.ARCONDICIONADO)
                .setComandos(new HashMap<>());
        mar.getModelos().put(mod.getId(), mod);

        mod.getComandos().put("poweroff",
                new Comando()
                        .setId("poweroff")
                        .setNome("Power Off")
                        .setRawData(Arrays.asList(
                                6134, 7364, 548, 1664, 548, 1634, 546, 1678, 536, 1632, 546, 1684, 530, 1634, 524, 1700, 536, 1634, 524, 640, 530, 568, 520, 598, 572, 568, 550, 566, 574, 586, 534, 568, 522, 640, 528, 1632, 524, 1706, 530, 1634, 522, 1666, 574, 1632, 520, 1710, 530, 1634, 522, 1664, 574, 566, 552, 570, 570, 570, 550, 566, 524, 638, 532, 568, 522, 598, 574, 566, 566, 1620, 572, 1632, 522, 1664, 576, 1632, 630, 1556, 572, 1636, 552, 1634, 546, 1662, 550, 564, 550, 612, 534, 568, 522, 640, 530, 566, 522, 598, 574, 566, 550, 568, 522, 1704, 532, 1632, 546, 612, 536, 1634, 522, 1704, 534, 1634, 522, 1704, 532, 1682, 476, 638, 530, 568, 520, 1710, 528, 566, 522, 598, 576, 588, 530, 566, 548, 612, 534, 1634, 522, 1702, 560, 1634, 546, 612, 534, 1634, 524, 638, 530, 1634, 522, 1702, 534, 570, 520, 648, 524, 616, 500, 1634, 576, 566, 550, 1632, 574, 584, 536, 568, 524, 636, 532, 1634, 522, 640, 530, 1634, 522, 598, 574, 1682, 472, 600, 572, 568, 550, 1634, 548, 610, 534, 1634, 524, 638, 532, 1634, 524, 642, 530, 1632, 522, 1706, 530, 7394, 574
                        )));
        mod.getComandos().put("cool20",
                new Comando()
                        .setId("cool20")
                        .setNome("COOL 20°C")
                        .setRawData(Arrays.asList(
                                6122, 7370, 574, 1634, 550, 1632, 576, 1638, 548, 1636, 546, 1684, 528, 1634, 524, 1702, 534, 1636, 520, 642, 530, 568, 518, 600, 574, 582, 534, 568, 546, 616, 532, 566, 520, 648, 526, 1634, 550, 1636, 572, 1636, 550, 1632, 576, 1654, 530, 1634, 548, 1680, 534, 1634, 524, 638, 532, 566, 522, 598, 574, 566, 550, 568, 546, 612, 534, 568, 522, 642, 528, 1632, 522, 1710, 530, 1632, 550, 1638, 574, 1634, 566, 1618, 576, 1620, 564, 1634, 548, 614, 532, 566, 524, 644, 526, 568, 522, 596, 574, 590, 528, 570, 522, 638, 534, 1632, 524, 646, 526, 566, 520, 1664, 576, 1632, 550, 1634, 574, 1634, 550, 1634, 546, 610, 538, 1634, 522, 1704, 534, 568, 520, 648, 526, 566, 550, 568, 548, 612, 534, 1634, 524, 1700, 562, 568, 524, 1702, 534, 1636, 522, 640, 530, 1634, 522, 1666, 574, 566, 522, 596, 576, 1654, 530, 568, 548, 628, 518, 1634, 524, 642, 528, 566, 522, 596, 576, 1654, 532, 566, 548, 1676, 536, 568, 522, 1706, 532, 568, 522, 596, 574, 1634, 550, 566, 576, 1636, 552, 566, 524, 1702, 534, 568, 522, 1736, 502, 1634, 522, 7448, 552
                        )));
        mod.getComandos().put("cool21",
                new Comando()
                        .setId("cool21")
                        .setNome("COOL 21°C")
                        .setRawData(Arrays.asList(
                                6130, 7366, 526, 1700, 536, 1634, 524, 1706, 532, 1636, 520, 1666, 572, 1634, 550, 1636, 572, 1636, 552, 566, 546, 644, 504, 568, 522, 598, 572, 568, 540, 578, 576, 584, 534, 568, 522, 1704, 532, 1634, 522, 1706, 532, 1632, 524, 1706, 530, 1634, 522, 1664, 574, 1636, 566, 548, 550, 610, 534, 568, 524, 640, 530, 568, 540, 580, 572, 590, 530, 592, 500, 1702, 534, 1634, 524, 1706, 530, 1632, 524, 1706, 532, 1634, 520, 1712, 528, 1634, 566, 552, 548, 596, 548, 566, 524, 640, 530, 566, 524, 598, 574, 566, 550, 568, 524, 1688, 550, 592, 498, 642, 530, 1634, 520, 1664, 574, 1634, 570, 1618, 572, 1634, 550, 568, 546, 1664, 550, 1632, 526, 638, 532, 568, 520, 598, 574, 568, 550, 568, 524, 604, 566, 1636, 522, 638, 532, 1634, 572, 1662, 530, 566, 534, 1654, 572, 1634, 552, 1632, 548, 580, 566, 1632, 548, 580, 570, 566, 542, 1644, 576, 566, 552, 588, 542, 560, 572, 1630, 526, 598, 546, 1682, 500, 618, 504, 1706, 512, 606, 528, 618, 474, 1706, 522, 598, 502, 1706, 500, 620, 500, 1708, 476, 642, 528, 1684, 476, 1706, 532, 7438, 478
                        )));
        mod.getComandos().put("cool22",
                new Comando()
                        .setId("cool22")
                        .setNome("COOL 22°C")
                        .setRawData(Arrays.asList(
                                6128, 7366, 548, 1646, 568, 1632, 548, 1680, 534, 1634, 524, 1706, 532, 1634, 524, 1704, 532, 1634, 524, 598, 572, 568, 550, 566, 548, 642, 504, 566, 522, 642, 530, 568, 518, 604, 572, 1634, 552, 1634, 546, 1648, 566, 1682, 476, 1702, 534, 1634, 524, 1702, 534, 1636, 522, 600, 572, 566, 520, 596, 578, 586, 530, 566, 524, 636, 534, 568, 522, 598, 574, 1634, 538, 1646, 578, 1656, 530, 1632, 548, 1666, 548, 1634, 524, 1702, 534, 1634, 524, 640, 530, 568, 520, 598, 576, 588, 530, 566, 524, 638, 532, 616, 472, 644, 530, 1632, 538, 580, 576, 552, 566, 1634, 524, 1708, 528, 1634, 522, 1704, 534, 1632, 522, 644, 528, 1634, 518, 1668, 574, 566, 552, 566, 524, 632, 538, 566, 524, 600, 570, 1634, 522, 598, 576, 568, 550, 1634, 524, 1702, 534, 568, 522, 1706, 532, 1636, 522, 598, 572, 1634, 520, 1666, 572, 566, 552, 566, 524, 1702, 534, 566, 524, 642, 530, 568, 522, 1710, 528, 568, 566, 1616, 576, 584, 534, 1634, 546, 614, 532, 568, 520, 1708, 532, 566, 520, 1668, 572, 566, 552, 1634, 546, 618, 528, 1634, 522, 1700, 536, 7398, 574
                        )));
        mod.getComandos().put("cool23",
                new Comando()
                        .setId("cool23")
                        .setNome("COOL 23°C")
                        .setRawData(Arrays.asList(
                                6132, 7364, 526, 1702, 536, 1634, 520, 1706, 532, 1634, 522, 1666, 572, 1634, 518, 1668, 574, 1634, 550, 568, 548, 616, 530, 566, 524, 634, 536, 566, 520, 600, 572, 570, 548, 566, 548, 1662, 552, 1630, 526, 1690, 546, 1660, 498, 1702, 532, 1636, 522, 1702, 536, 1634, 522, 640, 530, 566, 522, 600, 570, 568, 552, 568, 524, 666, 502, 566, 522, 600, 572, 1634, 520, 1670, 572, 1634, 550, 1636, 574, 1632, 552, 1634, 572, 1634, 552, 1632, 548, 644, 504, 566, 522, 640, 532, 566, 520, 600, 574, 566, 550, 566, 548, 644, 502, 1634, 524, 640, 532, 566, 522, 1666, 574, 1632, 520, 1704, 538, 1632, 552, 1634, 576, 552, 566, 1632, 548, 1684, 530, 566, 524, 640, 530, 570, 518, 598, 576, 568, 548, 566, 526, 636, 532, 568, 522, 1704, 534, 1634, 520, 644, 526, 1634, 520, 1664, 576, 1632, 552, 1634, 574, 1634, 568, 550, 550, 610, 534, 1632, 526, 638, 532, 566, 522, 598, 574, 1634, 518, 600, 574, 1634, 552, 566, 524, 1706, 530, 566, 526, 596, 572, 1636, 522, 598, 574, 1632, 522, 598, 548, 1660, 552, 566, 524, 1700, 538, 1634, 524, 7446, 522
                        )));
        mod.getComandos().put("cool24",
                new Comando()
                        .setId("cool24")
                        .setNome("COOL 24°C")
                        .setRawData(Arrays.asList(
                                6208, 7290, 524, 1706, 532, 1632, 522, 1666, 572, 1632, 522, 1668, 574, 1632, 552, 1634, 576, 1634, 566, 550, 548, 610, 534, 568, 524, 640, 530, 568, 568, 554, 568, 570, 550, 568, 524, 1708, 528, 1634, 546, 1678, 536, 1634, 524, 1702, 534, 1634, 522, 1706, 532, 1634, 522, 640, 530, 568, 568, 552, 570, 568, 550, 566, 548, 610, 536, 566, 522, 642, 528, 1634, 520, 1710, 530, 1634, 522, 1710, 528, 1634, 522, 1666, 572, 1632, 570, 1660, 530, 568, 566, 552, 544, 612, 536, 568, 520, 640, 530, 568, 542, 578, 572, 568, 550, 1634, 548, 596, 550, 566, 522, 1704, 532, 1634, 522, 1706, 532, 1634, 522, 1708, 530, 568, 522, 1708, 530, 1634, 518, 602, 572, 568, 550, 566, 548, 612, 532, 566, 524, 1706, 532, 1634, 548, 1672, 566, 568, 522, 1704, 532, 570, 520, 1710, 530, 1632, 524, 598, 574, 568, 626, 490, 548, 1674, 538, 566, 524, 1700, 534, 568, 522, 642, 530, 566, 522, 1664, 574, 568, 550, 1636, 572, 570, 550, 1632, 546, 644, 504, 566, 524, 1706, 530, 566, 522, 1708, 530, 568, 568, 1618, 572, 568, 566, 1618, 574, 1636, 550, 7432, 536
                        )));
        mod.getComandos().put("cool25",
                new Comando()
                        .setId("cool25")
                        .setNome("COOL 25°C")
                        .setRawData(Arrays.asList(
                                6104, 7394, 522, 1666, 574, 1634, 520, 1708, 530, 1634, 520, 1666, 574, 1634, 550, 1634, 574, 1634, 568, 548, 574, 582, 538, 566, 524, 638, 530, 568, 522, 598, 574, 566, 568, 550, 572, 1620, 566, 1636, 546, 1686, 528, 1632, 548, 1678, 534, 1634, 522, 1702, 534, 1636, 522, 638, 530, 568, 522, 600, 572, 568, 570, 546, 576, 586, 534, 568, 522, 638, 532, 1634, 522, 1702, 534, 1632, 522, 1708, 530, 1634, 522, 1708, 530, 1634, 522, 1666, 572, 568, 568, 550, 572, 584, 536, 568, 522, 640, 530, 568, 520, 600, 650, 488, 568, 1616, 656, 486, 552, 566, 548, 1662, 550, 1632, 524, 1702, 534, 1632, 524, 1702, 534, 568, 522, 1708, 530, 1634, 522, 642, 530, 568, 568, 552, 570, 568, 550, 566, 524, 636, 532, 1632, 524, 1704, 534, 568, 520, 1712, 528, 568, 520, 1664, 574, 1634, 570, 1616, 574, 568, 550, 566, 524, 1700, 536, 566, 524, 1700, 536, 568, 522, 646, 526, 566, 568, 1618, 574, 568, 550, 1632, 576, 590, 530, 1632, 546, 614, 532, 568, 522, 1706, 532, 566, 522, 1708, 530, 568, 568, 1620, 572, 568, 550, 1636, 574, 1634, 550, 7432, 536
                        )));

        mar = new Marca()
                .setId("lg")
                .setNome("LG")
                .setModelos(new HashMap<>());
        marcas.put(mar.getId(), mar);

        mod = new Modelo()
                .setMarca(mar)
                .setId("55LH5750")
                .setNome("TV LED 55LH5750")
                .setTipo(EnumTipoEquipamento.TV)
                .setComandos(new HashMap<>());
        mar.getModelos().put(mod.getId(), mod);

        mod.getComandos().put("POWER",
                new Comando()
                        .setId("POWER")
                        .setNome("POWER")
                        .setRawData(Arrays.asList(
                                9044, 4490, 596, 588, 542, 596, 552, 1712, 552, 588, 550, 588, 552, 588, 552, 590, 548, 588, 552, 1712, 552, 1712, 554, 544, 594, 1714, 552, 1712, 554, 1712, 552, 1712, 554, 1712, 552, 1712, 552, 542, 596, 586, 554, 586, 552, 1712, 554, 544, 594, 586, 552, 586, 552, 586, 554, 1712, 552, 1714, 552, 1712, 552, 586, 552, 1710, 554, 1712, 554, 1668, 596, 40148, 9084, 2238, 598
                        )));

        mod.getComandos().put("VOLUP",
                new Comando()
                        .setId("VOLUP")
                        .setNome("VOLUP")
                        .setRawData(Arrays.asList(
                                9082, 4532, 554, 588, 552, 588, 550, 1712, 552, 588, 552, 586, 552, 586, 552, 588, 552, 586, 552, 1712, 554, 1668, 596, 586, 552, 1670, 594, 1670, 594, 1670, 596, 1712, 552, 1712, 552, 586, 552, 1712, 554, 588, 550, 588, 550, 588, 552, 542, 596, 588, 552, 588, 550, 1712, 554, 586, 552, 1712, 552, 1670, 594, 1714, 552, 1670, 596, 1712, 552, 1712, 554, 40114, 9084, 2238, 596
                        )));

        mod.getComandos().put("VOLDOWN",
                new Comando()
                        .setId("VOLDOWN")
                        .setNome("VOLDOWN")
                        .setRawData(Arrays.asList(
                                9042, 4490, 596, 588, 552, 588, 526, 1738, 526, 612, 550, 588, 552, 588, 552, 588, 550, 588, 552, 1712, 552, 1670, 594, 544, 596, 1714, 526, 1738, 552, 1712, 552, 1670, 594, 1714, 526, 1740, 526, 1694, 584, 598, 552, 588, 552, 586, 552, 588, 550, 586, 528, 612, 552, 588, 550, 586, 552, 1670, 594, 1716, 524, 1738, 552, 1714, 552, 1712, 528, 1738, 552, 40116, 9082, 2238, 596
                        )));

        mod.getComandos().put("CHUP",
                new Comando()
                        .setId("CHUP")
                        .setNome("CHUP")
                        .setRawData(Arrays.asList(
                                9066, 4510, 602, 536, 604, 536, 578, 1734, 538, 600, 556, 538, 576, 564, 570, 568, 570, 590, 550, 1694, 578, 1688, 570, 568, 572, 1692, 572, 1740, 534, 1686, 602, 1664, 572, 1696, 568, 568, 602, 582, 528, 612, 552, 540, 572, 566, 574, 588, 580, 582, 556, 584, 532, 1686, 574, 1690, 604, 1664, 598, 1668, 602, 1662, 600, 1666, 602, 1708, 554, 1666, 572, 40178, 9066, 2258, 572
                        )));

        mod.getComandos().put("CHDOWN",
                new Comando()
                        .setId("CHDOWN")
                        .setNome("CHDOWN")
                        .setRawData(Arrays.asList(
                                9082, 4492, 570, 612, 526, 570, 568, 1740, 526, 612, 526, 568, 570, 570, 570, 612, 528, 612, 526, 1694, 570, 1738, 548, 592, 526, 1740, 526, 1694, 570, 1738, 526, 1740, 526, 1740, 526, 1696, 568, 614, 550, 586, 554, 586, 526, 612, 606, 532, 552, 586, 528, 568, 570, 612, 528, 1740, 526, 1738, 552, 1714, 606, 1658, 526, 1740, 552, 1712, 552, 1716, 524, 40144, 9082, 2240, 570
                        )));

        mar = new Marca()
                .setId("panasonic")
                .setNome("Panasonic");
        marcas.put(mar.getId(), mar);
    }

    private static void criarEmissores() {
        Emissor emi = new Emissor()
                .setId("abcdefg")
                .setNome("Emissor TESTE");
        emissores.put(emi.getId(), emi);
    }

    private static void criarEquipamentos() {
        Equipamento eq = new Equipamento()
                .setId("ac_elgin_pr-cpr")
                .setModelo(marcas.get("elgin").getModelos().get("SRFI-12000-2"))
                .setNome("Ar Condicionado Elgin pr-cpr")
                .setEmissor(emissores.get("abcdefg"));
        equipamentos.put(eq.getId(), eq);

        eq = new Equipamento()
                .setId("tv_pr-cpr")
                .setModelo(marcas.get("lg").getModelos().get("55LH5750"))
                .setNome("TV LG pr-cpr")
                .setEmissor(emissores.get("abcdefg"));
        equipamentos.put(eq.getId(), eq);
    }

    private static void criarTransmissoes() {
        Transmissao tra = new Transmissao()
                .setComando(marcas.get("elgin").getModelos().get("SRFI-12000-2").getComandos().get("poweroff"))
                .setDtHrSubmissao(new Date())
                .setUsuario(usuarios.get("fa89sd8f09s0df"))
                .setEquipamento(equipamentos.get("ac_elgin_pr-cpr"));
        transmissoes.put("a", tra);

        tra = new Transmissao()
                .setComando(marcas.get("elgin").getModelos().get("SRFI-12000-2").getComandos().get("cool25"))
                .setDtHrSubmissao(new Date(new Date().getTime() - 60000))
                .setDtHrTransmissao(new Date(new Date().getTime() - 50000))
                .setUsuario(usuarios.get("fa89sd8f09s0df"))
                .setEquipamento(equipamentos.get("ac_elgin_pr-cpr"));
        transmissoes.put("b", tra);

        tra = new Transmissao()
                .setComando(marcas.get("lg").getModelos().get("55LH5750").getComandos().get("POWER"))
                .setDtHrSubmissao(new Date(new Date().getTime() - 60000))
                .setDtHrTransmissao(new Date(new Date().getTime() - 50000))
                .setUsuario(usuarios.get("fa89sd8f09s0df"))
                .setEquipamento(equipamentos.get("tv_pr-cpr"));
        transmissoes.put("c", tra);
    }

    private static void criarBase() {
        criarUsuario();

        criarGrupos();

        relacionaUsuarioGrupo();

        criarMarcas();

        criarEmissores();

        criarEquipamentos();

        criarTransmissoes();
    }
}