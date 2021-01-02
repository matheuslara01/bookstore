package br.com.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.com.livraria.dao.AutorDao;
import br.com.livraria.dao.LivroDao;
import br.com.livraria.model.Autor;
import br.com.livraria.model.Livro;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class LivroBean implements Serializable {
	
	Livro livro = new Livro();
	
	@Inject
	LivroDao livroDao;
	
	@Inject
	AutorDao autorDao;
	
	@Inject
	FacesContext context;
	
	private Integer autorId;
	private List<Livro> livros;
	
	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}
	
	public Integer getAutorId() {
		return autorId;
	}
	
	public Livro getLivro() {
		return livro;
	}
	
	public List<Livro> getLivros(){
		if(this.livros == null) {
			this.livros = livroDao.listaTodos();
		}
		return livros;
	}
	
	public List<Autor> getAutores(){
		return autorDao.listaTodos();
	}
	
	public List<Autor> getAutoresDoLivro(){
		return this.livro.getAutores();
	}
	
	public void carregarLivroPelaId() {
		this.livro = livroDao.buscaPorId(this.livro.getId());
	}
	
	public void gravarAutor() {
		Autor autor = autorDao.buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
		System.out.println("Escrito por: " + autor.getNome());
	}
	
	@Transactional
	public void gravar() {
		System.out.println("Gravando livro" + this.livro.getTitulo());
		
		if(livro.getAutores().isEmpty()) {
			context.addMessage("autor", new FacesMessage("Livro deve ter pelo menos um autor."));
			return;
		}
		
		if(this.livro.getId() == null) {
			livroDao.adiciona(this.livro);
			this.livros = livroDao.listaTodos();
		} else {
			livroDao.atualiza(this.livro);
			this.livros = livroDao.listaTodos();
		}
		
		this.livro = new Livro();
	}
	
	@Transactional
	public void remover(Livro livro) {
		System.out.println("Removendo livro");
		livroDao.remove(livro);
		this.livros = livroDao.listaTodos();
	}
	
	public void removerAutorDoLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}
	
	public void carregar(Livro livro) {
		System.out.println("Carregando livro");
		this.livro = this.livroDao.buscaPorId(livro.getId());
	}
	
	public String formAutor() {
		System.out.println("Chamada do formulário do autor");
		return "autor?faces-redirect=true";
	}
	
	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if(!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("ISBN deveria começar com 1"));
		}
	}
}
