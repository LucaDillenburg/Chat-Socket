package chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import org.omg.CORBA.Environment;

import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.net.*;
import java.io.*;
import javax.swing.JTextArea;
import cliente.*;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Chat extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5954771544569818020L;
	
	private JPanel contentPane;
	private JLabel lbNome;
	private JLabel lbIp;
	private JButton btnEnviar;
	private JButton btnSair;
	private JButton btnConectar;
	
	private JComboBox<String> cbxDestinatarios;
	private JTextField txtMensagem;	
	private static JTextField textNome;
	private static JTextField textIP;
	private JComboBox<String> cbxSalas;
	
	private static PrintWriter transmissor;
	private static BufferedReader receptor;	
	private static boolean conectado = false;
	private Cliente cliente;
	private Socket conexao;

	private JScrollPane scrollPane;
	private JTextArea texto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{					
					Chat frame = new Chat();
					frame.setVisible(true);
					textIP.setText("localhost");
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void escolherSala()
	{
		if(!conectado)
		{
			lbIp.setEnabled(false);
			textIP.setEnabled(false);
			String ip = textIP.getText();
			try 
			{				
				this.conexao = new Socket(ip, 12345);
				
				receptor = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
				transmissor = new PrintWriter(this.conexao.getOutputStream());
				
			
				String nomesSalas = receptor.readLine();

				int qtd = Integer.parseInt(nomesSalas.substring(0, nomesSalas.indexOf("/&*_/")));
				nomesSalas = nomesSalas.substring(nomesSalas.indexOf("/&*_/") + 5);
				
				String[] vetorString = new String[qtd];
				
				for(int i = 0; i<qtd; i++)
				{
					if(i == qtd - 1)
						vetorString[i] = nomesSalas;
					else
					{
						vetorString[i] = nomesSalas.substring(0, nomesSalas.indexOf("/&*_/"));
						nomesSalas = nomesSalas.substring(nomesSalas.indexOf("/&*_/") + 5);
					}
					
				}
				
				cbxSalas.removeAllItems();
				for(int i=0; i<vetorString.length; i++)
					cbxSalas.addItem(vetorString[i]);
				
				btnConectar.setText("Entrar na sala");
				cbxSalas.setEnabled(true);
			}
			catch (Exception e)
			{
				lbIp.setEnabled(true);
				textIP.setEnabled(true);
				JOptionPane.showMessageDialog(null, "Servidor não está ligado!");
				return;
			}
		
			conectado = true;
		}
	}

	public Chat() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				try
				{
					sair();
					conexao.close();
				} catch (Exception e)
				{}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 602, 462);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectado = false;
				
				txtMensagem.setEnabled(false);
				btnEnviar.setEnabled(false);
				cbxDestinatarios.setEnabled(false);
				btnSair.setEnabled(false);
				
				textNome.setText("");
				cbxDestinatarios.removeAllItems();
				btnConectar.setEnabled(true);
				
				sair();
				escolherSala();
			}
		});
		btnSair.setEnabled(false);
		
		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(btnConectar.getText().equals("Conectar"))
					escolherSala();
				else
				if(btnConectar.getText().equals("Entrar na sala"))
				{
					cbxSalas.setEnabled(false);
					String salaEscolhida = (String)cbxSalas.getSelectedItem();
					
					transmissor.println("s" + salaEscolhida);
					transmissor.flush();
					
					cbxDestinatarios.addItem("TODOS");
					textNome.setEnabled(true);
					lbNome.setEnabled(true);
					btnConectar.setText("Criar nick");
					
					texto.setText("");
				}else
				{
					textNome.setText(textNome.getText().trim());
					if(textNome.getText() == null || textNome.getText().isEmpty() || textNome.getText().indexOf("/&*_/")>-1)
					{
						JOptionPane.showMessageDialog(null, "Nick inválido!");
						return;
					}
					
					transmissor.println("n" + textNome.getText());
					transmissor.flush();
					
					try
					{
						if(receptor.readLine().equals("nick nOK"))
						{
							JOptionPane.showMessageDialog(null, "Esse nick jah existe nessa sala, digite outro...");
							return;
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					
					cliente = new Cliente(conectado, texto, receptor, scrollPane, cbxDestinatarios, textNome.getText());
			        cliente.start();
					
					textNome.setEnabled(false);
					btnConectar.setEnabled(false);
					txtMensagem.setEnabled(true);
					btnEnviar.setEnabled(true);
					cbxDestinatarios.setEnabled(true);
					btnSair.setEnabled(true);
				}
		}});
		
		textNome = new JTextField();
		textNome.setEnabled(false);
		textNome.setColumns(10);
		
		lbNome = new JLabel("Nome:");
		lbNome.setEnabled(false);
		lbNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textIP = new JTextField();
		textIP.setColumns(10);
		
		lbIp = new JLabel("IP:");
		lbIp.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		cbxSalas = new JComboBox<String>();
		cbxSalas.setEnabled(false);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(cbxSalas, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lbIp)
						.addComponent(lbNome))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(textIP)
						.addComponent(textNome, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(btnConectar)
					.addGap(40)
					.addComponent(btnSair)
					.addGap(29))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lbIp)
										.addComponent(textIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lbNome)
										.addComponent(textNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(btnSair, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
									.addComponent(btnConectar, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(cbxSalas, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		txtMensagem = new JTextField();
		txtMensagem.setEnabled(false);
		txtMensagem.setColumns(10);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setEnabled(false);
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try{
					String msg = txtMensagem.getText();
					
					if(msg.isEmpty())
					{
						JOptionPane.showMessageDialog(null, "Mensagem vazia!");
						return;
					}
					String nickDest = (String)cbxDestinatarios.getSelectedItem();
					
					transmissor.println("env"+nickDest+"/&*_/"+msg);
					transmissor.flush();
					
					txtMensagem.setText("");
					txtMensagem.requestFocus();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		
		cbxDestinatarios = new JComboBox<String>();
		cbxDestinatarios.setEnabled(false);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(4)
					.addComponent(cbxDestinatarios, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtMensagem, GroupLayout.PREFERRED_SIZE, 391, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addGap(5))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtMensagem, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbxDestinatarios, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(2))
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		texto = new JTextArea();
		texto.setEditable(false);
		scrollPane.setViewportView(texto);
	}

	protected void sair()
	{
		transmissor.println("z");
		transmissor.flush();
		this.cliente.parar();
	}
}
