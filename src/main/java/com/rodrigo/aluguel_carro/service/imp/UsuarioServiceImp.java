package com.rodrigo.aluguel_carro.service.imp;

import com.rodrigo.aluguel_carro.entity.Usuario;
import com.rodrigo.aluguel_carro.exceptions.ErroUsuarioException;
import com.rodrigo.aluguel_carro.repository.UsuarioRepository;
import com.rodrigo.aluguel_carro.service.UsuarioService;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsuarioServiceImp implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImp(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()){ // se volto vazio quer dizer que nao achou o email
            throw new ErroUsuarioException("Usuario nao encontrado pelo email informado");

        }

        return usuario.get();

    }

    @Override
    public Usuario salvar(Usuario usuario) {
        validar(usuario);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void deletar(Usuario usuario) {
        Objects.requireNonNull(usuario.getId());
        usuarioRepository.delete(usuario);
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public void validar(Usuario usuario) {
        if(usuario.getNome() == null || usuario.getNome().trim().equals("")){
            throw new ErroUsuarioException("Informe um nome Valido");
        }
        if(usuario.getSenha() == null || usuario.getSenha().trim().equals("")){
            throw new ErroUsuarioException("Informe um Usuario Valido");
        }

        validarEmail(usuario);
    }

    @Override
    public void validarEmail(Usuario usuario) {
        if(usuario.getEmail() == null || usuario.getEmail().trim().equals("")){
            throw new ErroUsuarioException("Informe um Email");
        }

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(usuario.getEmail());

        boolean existe = usuarioRepository.existsByEmail(usuario.getEmail());

        if(existe){
            throw new ErroUsuarioException("Ja existe um Usuario cadastrado com este email");
        }
        if (!matcher.matches()) {
            throw new ErroUsuarioException("Email Invalido");
        }
    }
}
