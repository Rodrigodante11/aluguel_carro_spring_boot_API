package com.rodrigo.aluguel_carro.repository;

import com.rodrigo.aluguel_carro.Utils.Criar;
import com.rodrigo.aluguel_carro.entity.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmUsuario(){

        Usuario usuario = Criar.usuario();

        usuario = usuarioRepository.save(usuario);

        assertThat(usuario.getId()).isNotNull();
    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmUsuarioSemNome(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Usuario usuario = Criar.usuario();
            usuario.setNome(null);
            usuarioRepository.save(usuario);
        });
    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmUsuarioSemEmail(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Usuario usuario = Criar.usuario();
            usuario.setEmail(null);
            usuarioRepository.save(usuario);
        });
    }

    @Test
    public void deveLancarUmaExcessaoAoTentarSalvarUmUsuarioSemSenha(){

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            Usuario usuario = Criar.usuario();
            usuario.setSenha(null);
            usuarioRepository.save(usuario);
        });
    }

    @Test
    public void deveDeletarUmUsuario(){
        Usuario usuario = Criar.usuario();
        entityManager.persist(usuario);

        usuario = entityManager.find(Usuario.class, usuario.getId());

        usuarioRepository.delete(usuario);

        Usuario usuarioInexistente;
        usuarioInexistente = entityManager.find(Usuario.class, usuario.getId());

        assertThat(usuarioInexistente).isNull();
    }

    @Test
    public void deveBuscarUmUsuarioPorId(){
        Usuario usuario = Criar.usuario();
        entityManager.persist(usuario);

        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());

        assertThat(usuarioExistente.isPresent()).isTrue();

    }
}
