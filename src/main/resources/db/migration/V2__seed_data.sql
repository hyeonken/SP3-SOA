INSERT INTO ativo (codigo, nome, classe, perfil_risco, retorno_esperado) VALUES
('CDB100', 'CDB Liquidez Diaria', 'RENDA_FIXA', 'CONSERVADOR', 6.20),
('TESOUROIPCA', 'Tesouro IPCA 2035', 'RENDA_FIXA', 'MODERADO', 7.80),
('FIIABC', 'FII Corporativo ABC', 'FUNDOS_IMOBILIARIOS', 'MODERADO', 9.10),
('ACAOXYZ', 'Acao XYZ Tecnologia', 'RENDA_VARIAVEL', 'ARROJADO', 15.50),
('ETFIVVB11', 'ETF S&P500', 'RENDA_VARIAVEL', 'ARROJADO', 12.40);

INSERT INTO carteira (nome, descricao, perfil_risco, retorno_esperado, risco_estimado) VALUES
('Carteira Conservadora', 'Combinacao focada em preservacao de capital', 'CONSERVADOR', 6.50, 2.10),
('Carteira Moderada', 'Equilibrio entre crescimento e protecao', 'MODERADO', 9.20, 5.60),
('Carteira Arrojada', 'Busca por altas taxas de retorno com maior volatilidade', 'ARROJADO', 13.80, 9.40);

INSERT INTO item_carteira (carteira_id, ativo_id, percentual)
SELECT c.id, a.id, 70.00 FROM carteira c JOIN ativo a ON c.nome = 'Carteira Conservadora' AND a.codigo = 'CDB100';
INSERT INTO item_carteira (carteira_id, ativo_id, percentual)
SELECT c.id, a.id, 30.00 FROM carteira c JOIN ativo a ON c.nome = 'Carteira Conservadora' AND a.codigo = 'TESOUROIPCA';

INSERT INTO item_carteira (carteira_id, ativo_id, percentual)
SELECT c.id, a.id, 40.00 FROM carteira c JOIN ativo a ON c.nome = 'Carteira Moderada' AND a.codigo = 'TESOUROIPCA';
INSERT INTO item_carteira (carteira_id, ativo_id, percentual)
SELECT c.id, a.id, 30.00 FROM carteira c JOIN ativo a ON c.nome = 'Carteira Moderada' AND a.codigo = 'FIIABC';
INSERT INTO item_carteira (carteira_id, ativo_id, percentual)
SELECT c.id, a.id, 30.00 FROM carteira c JOIN ativo a ON c.nome = 'Carteira Moderada' AND a.codigo = 'ETFIVVB11';

INSERT INTO item_carteira (carteira_id, ativo_id, percentual)
SELECT c.id, a.id, 40.00 FROM carteira c JOIN ativo a ON c.nome = 'Carteira Arrojada' AND a.codigo = 'ACAOXYZ';
INSERT INTO item_carteira (carteira_id, ativo_id, percentual)
SELECT c.id, a.id, 30.00 FROM carteira c JOIN ativo a ON c.nome = 'Carteira Arrojada' AND a.codigo = 'ETFIVVB11';
INSERT INTO item_carteira (carteira_id, ativo_id, percentual)
SELECT c.id, a.id, 30.00 FROM carteira c JOIN ativo a ON c.nome = 'Carteira Arrojada' AND a.codigo = 'FIIABC';
