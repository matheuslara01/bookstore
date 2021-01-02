package br.com.livraria.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.livraria.dao.UsuarioDao;
import br.com.livraria.model.Usuario;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class LoginBean implements Serializable {
	
	Usuario usuario = new Usuario();
	
	@Inject
	UsuarioDao dao;
	
	@Inject
	FacesContext context;
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public String efetuaLogin() {
		System.out.println("Fazendo login do usuario" + this.usuario.getEmail());
		
		boolean existe = dao.existe(this.usuario);
		
		if(existe) {
			context.getExternalContext().getSessionMap().put("usuariologado", this.usuario);
			return "livro?faces-redirect=true";
		}
		
		context.getExternalContext().getFlash().setKeepMessages(true);
		context.addMessage(null, new FacesMessage("Usuário não encontrado"));
		
		return "login?faces-redirect=true";
	}
	
	public String deslogar() {
		context.getExternalContext().getSessionMap().remove("usuariologado");
		return "login?faces-redirect=true";
	}
}
