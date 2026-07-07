CREATE TABLE tipos_usuario (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE enderecos (
    id UUID PRIMARY KEY,
    logradouro VARCHAR(255) NOT NULL,
    numero VARCHAR(50) NOT NULL,
    complemento VARCHAR(255),
    bairro VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    uf VARCHAR(10) NOT NULL,
    cep VARCHAR(20) NOT NULL,
    data_ultima_alteracao TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE usuarios (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_ultima_alteracao TIMESTAMP WITH TIME ZONE NOT NULL,
    tipo_usuario_id UUID NOT NULL,
    endereco_id UUID NOT NULL,

    CONSTRAINT fk_usuario_tipo_usuario
        FOREIGN KEY (tipo_usuario_id)
        REFERENCES tipos_usuario(id),

    CONSTRAINT fk_usuario_endereco
        FOREIGN KEY (endereco_id)
        REFERENCES enderecos(id)
);

CREATE TABLE restaurantes (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    endereco_id UUID NOT NULL,
    data_ultima_alteracao TIMESTAMP WITH TIME ZONE NOT NULL,
    tipo_cozinha VARCHAR(255) NOT NULL,
    dono_restaurante_id UUID NOT NULL,

    CONSTRAINT fk_restaurante_endereco
        FOREIGN KEY (endereco_id)
        REFERENCES enderecos(id),

    CONSTRAINT fk_restaurante_dono
        FOREIGN KEY (dono_restaurante_id)
        REFERENCES usuarios(id)
);

CREATE TABLE horarios_funcionamento_restaurante (
    id UUID PRIMARY KEY,
    restaurante_id UUID NOT NULL,
    dias_semana VARCHAR(20) NOT NULL,
    hora_abertura TIME NOT NULL,
    hora_encerramento TIME NOT NULL,

    CONSTRAINT fk_horario_funcionamento_restaurante
        FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_horario_funcionamento_periodo
        CHECK (hora_abertura < hora_encerramento)
);

CREATE TABLE itens_cardapio (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(400) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    restaurante_id UUID NOT NULL,
    caminho_foto VARCHAR(255) NOT NULL,
    disponivel_apenas_no_restaurante BOOLEAN NOT NULL,
    data_ultima_alteracao TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_item_cardapio_restaurante
        FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_item_cardapio_preco_positivo
        CHECK (preco > 0)
);
