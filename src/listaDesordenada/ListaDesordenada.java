package listaDesordenada;

import java.lang.reflect.*;

public class ListaDesordenada<X> implements Cloneable
{
	protected class No
	{
		protected X  info;
		protected No prox;

		// nao precisa ter cuidados ante-anta (throws Exception, metodos obrigatorios), pois a classe é protected

		// construtores
		public No(X x)
		{
			this.info = x;
		}

		public No(X x, No no)
		{
			this(x);
			this.prox = no;
		}


		// getters and setters
		public void setInfo(X x)
		{
			this.info = x;
		}

		public void setProx(No no)
		{
			this.prox = no;
		}

		public X getInfo()
		{
			return this.info;
		}

		public No getProx()
		{
			return this.prox;
		}
	}
	
	protected No prim = null;
	protected No ultimo = null;
	protected No atualLista = null;
	protected int qtd = 0;

	// construtor
	public ListaDesordenada()
	{}

	//outros
	protected X meuCloneDeX (X x)
    {
        X ret = null;

        try
        {
            Class<?> classe = x.getClass();
            Class<?>[] tipoDoParametroFormal = null; // pq clone tem 0 parametros
            Method metodo = classe.getMethod ("clone", tipoDoParametroFormal);
            Object[] parametroReal = null;// pq clone tem 0 parametros
            ret = (X)metodo.invoke (x, parametroReal);
        }
        catch (Exception e)
        {}

        return ret;
    }

    public X getInicio() throws Exception
    {
    	if(this.prim==null)
    		throw new Exception("Lista nula!");

    	if(this.prim.getInfo() instanceof Cloneable)
    		return meuCloneDeX(this.prim.getInfo());
    	else
    		return this.prim.getInfo();
    }

    public X getUltimo() throws Exception
    {
    	if(this.prim==null)
    		throw new Exception("Lista nula!");

    	if(this.ultimo.getInfo() instanceof Cloneable)
    		return meuCloneDeX(this.ultimo.getInfo());
    	else
    		return this.ultimo.getInfo();
    }

    public int getQtd()
    {
    	return this.qtd;
    }

    public X  getAtualLista() throws Exception
    {
    	if(this.atualLista==null)
    		throw new Exception("Atual lista nulo");

    	if(this.atualLista.getInfo() instanceof Cloneable)
    		return meuCloneDeX(this.atualLista.getInfo());
    	else
    		return this.atualLista.getInfo();
    }
    
    public X  getProxAtualLista() throws Exception
    {
    	if(this.atualLista.getProx()==null)
    		throw new Exception("Atual lista nulo");

    	return this.atualLista.getProx().getInfo();
    }

    public void mudarAtualLista(int pos) throws Exception
    {
    	No atual = this.prim;
    	int i = 0;

    	if(pos<0 || pos>this.qtd-1)
    		throw new Exception("Essa posição não existe na lista.");

    	while(true)
    	{
    		if(i==pos)
    		{
    			this.atualLista = atual;
    			break;
    		}

    		i++;
    		atual = atual.getProx();
    	}
    }

    public void andarAtualLista() throws Exception
    {
    	if(this.atualLista == null)
    		throw new Exception("Atual lista nulo!");

    	this.atualLista = this.atualLista.getProx();
    }

	public void inserirNoInicio(X x) throws Exception
	{
		if(x==null)
			throw new Exception("Informacao nula!");

		X info;
		if(x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;
		
		this.prim = new No(info, this.prim);
		this.qtd++;
	}

	public void inserirNoFim(X x) throws Exception
	{
		if(x==null)
			throw new Exception("Informacao nula!");

		X info;
		if(x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;

		// lista vazia
		if(this.prim==null)
			this.prim = new No(info);
		else
			this.ultimo.setProx(new No(info));

		this.qtd++;
	}

	public void inserirPosAtual(X x) throws Exception
	{
		if(this.atualLista == null)
			throw new Exception("Atual lista nulo!");
		if(x == null)
			throw new Exception("Informacao nula!");

		X info;
		if(x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;

		this.atualLista.setProx(new No(info, this.atualLista.getProx()));
		this.qtd++;
	}

	public void jogarForaPrimeiro() throws Exception
    {
    	if(this.prim==null)
    		throw new Exception("Lista vazia");

    	this.prim = this.prim.getProx();
    	this.qtd--;
    }

    public void jogarForaUltimo() throws Exception
    {
    	if(this.prim==null)
    		throw new Exception("Lista vazia!");

    	if(this.prim.getProx()!=null)
    	{
    		No atual = this.prim;

    		//vai ateh o penultimo
	    	while(atual.getProx().getProx()!=null)
	    		atual = atual.getProx();
	    	
	    	atual.setProx(null);
	    	this.ultimo = atual;
    	}else
    		this.prim = null;

    	this.qtd--;
    }

    public void jogarForaProxAtualLista() throws Exception
    {
    	if(this.prim==null)
    		throw new Exception("Lista vazia!");
    	if(this.atualLista == null)
    		throw new Exception("Atual lista nulo!");
    	if(this.atualLista.getProx() == null)
    		throw new Exception("No a ser excluido nao existe!");

    	if(this.prim.getProx() == null)
    		this.prim = null;
    	else
    		this.atualLista.setProx(this.atualLista.getProx().getProx());    		

    	this.qtd--;
    }

    // metodos obrigatorios
	public String toString()
	{
		String ret = "{";
		No     atual = this.prim;

		while(atual!=null)
		{
			ret+=atual.getInfo();

			if(atual.getProx()!=null)
			// se atual nao eh o ultimo
				ret+=", ";

			atual = atual.getProx();
		}

		ret += "}";

		return ret;
	}

	public int hashCode()
	{
		int ret = 1;

		No atual = this.prim;

		while(atual!=null)
		{
			ret = 7*ret + atual.getInfo().hashCode();

			atual = atual.getProx();
		}

		return ret;
	}

	public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		if(this==obj)
			return false;

		if(!(obj instanceof ListaDesordenada))
			return false;

		ListaDesordenada<X> lista = (ListaDesordenada<X>)obj;

		No atual  = this.prim;
		No atual2 = lista.prim;

		while(atual!=null && atual2!=null)
		// ateh uma das listar acabar
		{
			if(!atual.getInfo().equals(atual2.getInfo()))
				return false;
			atual  = atual.getProx();
			atual2 = atual2.getProx();
		}

		if(atual==null && atual2==null)
			return true;

		return false;
	}

    public ListaDesordenada(ListaDesordenada<X> modelo) throws Exception
	{
		if(modelo==null)
			throw new Exception("Modelo nulo!");

		if(modelo.prim!=null)
		{
			this.prim = new No(modelo.prim.getInfo());
			
			No atual = this.prim;
			No atual2 = this.prim.getProx();

			while(atual2!=null)
			{
				atual.setProx(new No(atual2.getInfo()));

				atual  = atual.getProx();
				atual2 = atual2.getProx();
			}
		}
	}

	public Object clone()
	{
		ListaDesordenada<X> lista = null;

		try
		{
			lista = new ListaDesordenada<X>(this);
		}catch(Exception e)
		{}

		return lista;
	}
}
