package cliente;

import java.io.*;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;

public class Cliente extends Thread
{
	
	private boolean conectado = false;
	public String msg = "";
	private JTextArea texto;
	private BufferedReader receptor;
	private JScrollPane scrllPane;
	private JComboBox<String> cbxDest;
	private String nick;
	private boolean fim = false;
	
	public Cliente(boolean conectado, JTextArea texto, BufferedReader receptor, JScrollPane scrollPane, JComboBox<String> cbx, String n)
	{
		this.conectado = conectado;
		this.texto = texto;
		this.receptor = receptor;
		this.scrllPane = scrollPane;
		this.cbxDest = cbx;
		this.nick = n;
		
		this.texto.removeAll();
		this.texto.append("Nick OK\r\n");
	}
	
	public void run()
	{
		while(!fim)
        {
			try
			{
	        	if(conectado)
	        	{
	        		msg = receptor.readLine();
	        	
		        	if(msg.charAt(0) == '+')
		        	{
		        		texto.append(msg.substring(1)+" entrou\r\n");
		        		if(!msg.substring(1).equals(this.nick))
		        			this.cbxDest.addItem(msg.substring(1));
		        	}else
		            if(msg.charAt(0) == '-')
		            {
		            	texto.append(msg.substring(1)+" saiu\r\n");
		            	this.cbxDest.removeItem(msg.substring(1));
		            }else
		            if(msg.substring(0, 3).equals("msg"))
		        		this.texto.append(msg.substring(3)+"\r\n");
		            else
		            	this.texto.append("ERRO!- Cliente recebeu: "+msg);
		        	
		        	JScrollBar vertical = scrllPane.getVerticalScrollBar();
		        	vertical.setValue(vertical.getMaximum());
	        	}
			} catch (Exception e)
			{}
        }
	}
	
	public String getMsg()
	{
		return msg;
	}
	
	public void parar()
	{
		fim = true;
	}
}
