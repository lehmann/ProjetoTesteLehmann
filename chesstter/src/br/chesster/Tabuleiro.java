package br.chesster;

import br.chesster.Tabuleiro.Posicionamento;

public class Tabuleiro {

	private Peca[][] pecas;
	private Jogador brancas;
	private Jogador pretas;
	private Posicionamento posicionamento;
	
	public Tabuleiro() {
		pecas = new Peca[8][8];
	}
	
	public void iniciarPartida(Jogador brancas, Jogador pretas) {
		this.brancas = brancas;
		this.pretas = pretas;
		this.posicionamento = new Posicionamento();
		posicionamento.inicioPartida(brancas, pretas);
	}
	
	Jogador posicionarJogador() {
		if(brancas != null && pretas != null) {
			throw new IllegalStateException("Todos os jogadores já foram posicionados");
		}
		if(brancas == null) {
			brancas = new Jogador();
			return brancas;
		} else {
			pretas = new Jogador();
			return pretas;
		}
	}
	
	private class Posicionamento {
		void inicioPartida(Jogador brancas, Jogador pretas) {
			//construindo peões
			for (int i = 0; i < 8; i++) {
				Peao peaoBranco = new Peao();
				brancas.adicionarPeca(2, i + 1, peaoBranco);
				pecas[1][i] = peaoBranco;
				Peao peaoPreto = new Peao();
				pretas.adicionarPeca(7, i + 1, peaoPreto);
				pecas[6][i] = peaoPreto;
			}
			//construindo torres
			Torre torreBranca = new Torre();
			brancas.adicionarPeca(1, 1, torreBranca);
			pecas[0][0] = torreBranca;
			Torre torrePreta = new Torre();
			pretas.adicionarPeca(8, 1, torrePreta);
			pecas[7][0] = torrePreta;
			Torre torreBranca_2 = new Torre();
			brancas.adicionarPeca(1, 8, torreBranca_2);
			pecas[0][7] = torreBranca_2;
			Torre torrePreta_2 = new Torre();
			pretas.adicionarPeca(8, 8, torrePreta_2);
			pecas[7][7] = torrePreta_2;
			//construindo cavalos
			Cavalo cavaloBranco = new Cavalo();
			brancas.adicionarPeca(1, 2, cavaloBranco);
			pecas[0][1] = cavaloBranco;
			Cavalo cavaloPreto = new Cavalo();
			pretas.adicionarPeca(8, 2, cavaloPreto);
			pecas[7][1] = cavaloPreto;
			Cavalo cavaloBranco_2 = new Cavalo();
			brancas.adicionarPeca(1, 7, cavaloBranco_2);
			pecas[0][6] = cavaloBranco_2;
			Cavalo cavaloPreto_2 = new Cavalo();
			pretas.adicionarPeca(8, 7, cavaloPreto_2);
			pecas[7][6] = cavaloPreto_2;
			//construindo bispos
			Bispo bispoBranco = new Bispo();
			brancas.adicionarPeca(1, 3, bispoBranco);
			pecas[0][2] = bispoBranco;
			Bispo bispoPreto = new Bispo();
			pretas.adicionarPeca(8, 3, bispoPreto);
			pecas[7][2] = bispoPreto;
			Bispo bispoBranco_2 = new Bispo();
			brancas.adicionarPeca(1, 6, bispoBranco_2);
			pecas[0][5] = bispoBranco_2;
			Bispo bispoPreto_2 = new Bispo();
			pretas.adicionarPeca(8, 6, bispoPreto_2);
			pecas[7][5] = bispoPreto_2;
			//construindo rainhas
			Rainha rainhaBranca = new Rainha();
			brancas.adicionarPeca(1, 4, rainhaBranca);
			pecas[0][3] = rainhaBranca;
			Rainha rainhaPreta = new Rainha();
			pretas.adicionarPeca(8, 4, rainhaPreta);
			pecas[7][3] = rainhaPreta;
			//construindo reis
			Rei reiBranco = new Rei();
			brancas.adicionarPeca(1, 5, reiBranco);
			pecas[0][4] = reiBranco;
			Rei reiPreto = new Rei();
			pretas.adicionarPeca(8, 5, reiPreto);
			pecas[7][4] = reiPreto;
		}
	}
}
