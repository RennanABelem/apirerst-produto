package com.produtos.apirest.endpoint;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produtos.apirest.error.ResourceNotFoundException;
import com.produtos.apirest.models.Categoria;
import com.produtos.apirest.models.Produto;
import com.produtos.apirest.repository.ProdutoRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api")
@Api(value = "API REST Produtos")
@CrossOrigin("*")
public class ProdutoEndpoint {

	@Autowired
	private ProdutoRepository repository;

	@GetMapping("/produtos")
	@ApiOperation(value = "Retorna uma lista de produtos")
	public ResponseEntity<?> listarProdutos() {
		
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/produto/{id}")
	@ApiOperation(value = "Retorna apenas um produto com base no ID")
	public ResponseEntity<?> listarUmUnicoProduto(@PathVariable(value = "id") long id) {
		verifyIfExistProduto(id);
		return new ResponseEntity<>(repository.findById(id), HttpStatus.OK);
	}

	@PostMapping("/produto")
	@ApiOperation(value = "Salva um produto")
	public ResponseEntity<?> salvarProduto(@Valid @RequestBody Produto produto) {
		
		addAuxProdECate(produto);
		return new ResponseEntity<>(repository.save(produto), HttpStatus.CREATED);
	}

	@DeleteMapping("/produto/{id}")
	@ApiOperation(value = "Deleta um produto baseado no ID")
	public ResponseEntity<?> deletarProduto(@PathVariable(value = "id")long id) {
		
		verifyIfExistProduto(id);
		
		repository.deleteById(id);
		return new ResponseEntity<>("Produto deletado com sucesso", HttpStatus.OK);
	}

	@PutMapping("/produto")
	@ApiOperation(value = "Atualiza os dados do produto")
	public ResponseEntity<?> alterarProduto(@RequestBody Produto produto) {
		verifyIfExistProduto(produto.getId());
		addAuxProdECate(produto);
		return new ResponseEntity<>(repository.save(produto), HttpStatus.OK);
	}
	
	public void verifyIfExistProduto(Long id) {
		if(!repository.findById(id).isPresent()) {
			throw new ResourceNotFoundException("Não foi possivel achar o produto com o id " + id);
		}
	}
	private void addAuxProdECate(Produto produto) {
		for (Categoria c : produto.getCategorias()) {
			c.setProduto(produto);
		}
	}

}
