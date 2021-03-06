package Atendente;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.SystemColor;

public class Pesquisa_de_clientes extends JFrame {

	private JPanel contentPane;
	private JTable table;
	Cliente var = new Cliente();
	private JTextField tf_nome;
	private String cliente;
	private String vendedor;
	private String produto;
	private String sessao;

	/**
	 * Launch the application.
	 */
	public void pesquisarCliente() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pesquisa_de_clientes frame = new Pesquisa_de_clientes();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Pesquisa_de_clientes() {
		setTitle("Pesquisa de clientes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1100, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 1082, 753);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 460, 1082, 236);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setBounds(20, 398, 687, -181);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
			},
			new String[] {
				"Id", "Nome", "Cpf", "Rg"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		scrollPane.setViewportView(table);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Cheap Potatoes", Font.PLAIN, 11));
		btnCancelar.setBackground(SystemColor.activeCaption);
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setBounds(213, 709, 119, 31);
		panel.add(btnCancelar);
		
		JButton btnInserirDados = new JButton("Inserir dados");
		btnInserirDados.setFont(new Font("Cheap Potatoes", Font.PLAIN, 11));
		btnInserirDados.setBackground(SystemColor.activeCaption);
		btnInserirDados.setForeground(Color.WHITE);
		btnInserirDados.setBounds(712, 709, 154, 31);
		panel.add(btnInserirDados);
		
		tf_nome = new JTextField();
		tf_nome.setBounds(446, 257, 462, 30);
		panel.add(tf_nome);
		tf_nome.setColumns(10);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("C:\\Users\\Thiago\\workspace\\bird_shoes\\src\\Trabalho_Software\\Imagens\\pesquisadeclientes.png"));
		label.setBounds(0, 0, 1082, 753);
		panel.add(label);
		
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Venda v = new Venda();
					v.setCpfCliente(enviaCliente());
					v.setCpfVendedor(enviaVendedor());
					v.setIdProduto(enviaProduto());
					v.setSessao(enviaSessao());
					v.setVisible(true);
					dispose();
				}catch(Exception erro) {
					
					System.out.println(erro);
				}
			}
		});

		btnInserirDados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Venda v = new Venda();
					int linha = table.getSelectedRow();
					if(linha > -1){
						String cpf = String.valueOf(table.getValueAt(linha, 2));
						v.setCpfCliente(cpf);
						v.setIdProduto(enviaProduto());
						v.setCpfVendedor(enviaVendedor());
						v.setSessao(enviaSessao());
						v.setVisible(true);
						dispose();
					}else{
						JOptionPane.showMessageDialog(null, "Selecione uma linha da tabela", "Selecione a linha", JOptionPane.WARNING_MESSAGE);	
					}
				}catch(Exception erro) {
					
					System.out.println(erro);
				}
			}
		});
		
		tf_nome.addKeyListener(new java.awt.event.KeyAdapter(){
			
			public void keyReleased(java.awt.event.KeyEvent e){
				if(tf_nome.getText().length()>=1);
				pesquisar();
			}
		});

	}
	
	public void recebeCampos(String cpfCliente, String cpfVendedor, String idProduto, String sessao){
		cliente = cpfCliente;
		vendedor = cpfVendedor;
		produto = idProduto;
		this.sessao = sessao;
	}
	public String enviaCliente(){
		return cliente;
	}
	public String enviaVendedor(){
		return vendedor;
	}
	public String enviaProduto(){
		return produto;
	}
	public String enviaSessao(){
		return sessao;
	}
	
	public void pesquisar() {
		try {
			Conexao con = new Conexao();
			var.setNome(tf_nome.getText());
			con.setSql("Select id, nome, cpf, rgCliente from cliente where nome like '"+var.getNome()+"%' order by nome");
			con.setAcao(con.getConexao().prepareStatement(con.getSql()));
			con.setResultado(con.getAcao().executeQuery());
			
			DefaultTableModel modelo = (DefaultTableModel) table.getModel();
			modelo.setNumRows(0);
			while(con.getResultado().next()) {
				modelo.addRow(new Object[] {con.getResultado().getString("id"), con.getResultado().getString("nome"),con.getResultado().getString("cpf"),
					con.getResultado().getString("rgCliente")});
			}
			con.getResultado().close();
		}catch(Exception erro) {
			System.out.println(erro);
		}
	}
}