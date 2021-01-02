package br.com.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.com.livraria.dao.AutorDao;
import br.com.livraria.model.Autor;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class AutorBean implements Serializable {
	
	Autor autor = new Autor();
	
	@Inject
	private AutorDao dao;
	
	private Integer autorId;
	
	public Integer getAutorId() {
		return autorId;
	}
	
	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}
	
	public void carregarAutorPelaId() {
		this.autor = this.dao.buscaPorId(autorId);
	}
	
	@Transactional
	public String gravar() {
		System.out.println("Gravando autor" + this.autor.getNome());
		
		if(this.autor.getId() == null) {
			this.dao.adiciona(this.autor);
		} else {
			this.dao.atualiza(this.autor);
		}
		
		this.autor = new Autor();
		
		return "livro?faces-redirect=true";
	}
	
	@Transactional
	public void remover(Autor autor) {
		System.out.println("Removendo autor" + autor.getNome());
		this.dao.remove(autor);
	}
	
	public void carregar(Autor autor) {
		System.out.println("Carregando autor");
		this.autor = this.dao.buscaPorId(autor.getId());
	}
	
	public List<Autor> getAutores(){
		return this.dao.listaTodos();
	}
	
	public Autor getAutor() {
		return autor;
	}
	
	public void setAutor(Autor autor) {
		this.autor = autor;
	}
	
}
