package com.rodrigo.aluguel_carro.services;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.repository.UsuarioRepository;
import com.rodrigo.aluguel_carro.service.imp.UsuarioServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // Cria uma instância do banco de dados em memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioService {

    @SpyBean
    UsuarioServiceImp usuarioServiceImp;

    @MockBean
    UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

//    @MockBean
//    private JwtService jwtService;

    @Test
    public void devesalvarUmUsuario(){
        Usuario usuarioASalvar = Criar.usuario();

        Assertions.assertDoesNotThrow(() -> {
            Usuario usuarioSalvo = Criar.usuario();
            usuarioSalvo.setId(1L);

            Mockito.doNothing().when(usuarioServiceImp).validar(usuarioASalvar);
            Mockito.doNothing().when(usuarioServiceImp).cripografarSenha(usuarioSalvo);

            Mockito.when(usuarioRepository.save(usuarioASalvar)).thenReturn(usuarioSalvo);

            Usuario usuarioSalvoImp = usuarioServiceImp.salvar(usuarioASalvar);

            assertThat(usuarioSalvoImp).isNotNull();
            assertThat(usuarioSalvoImp.getId()).isEqualTo(usuarioSalvo.getId());


        });

    }

    @Test
    public void deveDeletarUmUsuario(){
        Usuario usuarioSalvo = Criar.usuario();
        usuarioSalvo.setId(1L);

        usuarioServiceImp.deletar(usuarioSalvo);

        Mockito.verify(usuarioRepository).delete(usuarioSalvo);
    }

    @Test
    public void deveLancarUmErroAoDeletarUmUsuarioNaoSalva(){
        Usuario usuario = Criar.usuario();

        Assertions.assertThrows(NullPointerException.class, () -> usuarioServiceImp.deletar(usuario));
        Mockito.verify(usuarioRepository, Mockito.never()).delete(usuario);
    }

    @Test
    public void deveObterUmUsuarioPorId(){
        Usuario usuario = Criar.usuario();
        Long id = 1L;
        usuario.setId(1L);

        // nao quero testar o metodo (findById)
        // apenas disse quando ele foi chamado para retornar o Usuario direto pois nao estou testando esse metodo
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioServiceImp.obterPorId(id);

        assertThat(resultado.isPresent()).isTrue();

        Mockito.verify(usuarioRepository).findById(id);
    }
}
