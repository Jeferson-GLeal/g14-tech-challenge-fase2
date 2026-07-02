INSERT INTO tipos_usuario (id, nome, codigo)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Dono de Restaurante', 'DONO_RESTAURANTE'),
    ('22222222-2222-2222-2222-222222222222', 'Cliente', 'CLIENTE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO enderecos (
    id,
    logradouro,
    numero,
    complemento,
    bairro,
    cidade,
    uf,
    cep,
    data_ultima_alteracao
)
VALUES
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
        'Rua Vergueiro',
        '1200',
        'Sala 5',
        'Vila Mariana',
        'Sao Paulo',
        'SP',
        '04101-000',
        CURRENT_TIMESTAMP
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
        'Avenida Brigadeiro Faria Lima',
        '1500',
        'Conjunto 42',
        'Jardim Paulistano',
        'Sao Paulo',
        'SP',
        '01452-001',
        CURRENT_TIMESTAMP
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb3',
        'Rua Harmonia',
        '300',
        'Casa',
        'Vila Madalena',
        'Sao Paulo',
        'SP',
        '05435-000',
        CURRENT_TIMESTAMP
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb4',
        'Rua Oscar Freire',
        '900',
        'Loja 12',
        'Jardins',
        'Sao Paulo',
        'SP',
        '01426-002',
        CURRENT_TIMESTAMP
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb5',
        'Avenida Ipiranga',
        '200',
        'Loja Terrea',
        'Republica',
        'Sao Paulo',
        'SP',
        '01046-010',
        CURRENT_TIMESTAMP
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb6',
        'Rua Pamplona',
        '700',
        'Loja 3',
        'Jardim Paulista',
        'Sao Paulo',
        'SP',
        '01405-001',
        CURRENT_TIMESTAMP
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (
    id,
    nome,
    email,
    login,
    senha,
    data_ultima_alteracao,
    tipo_usuario_id,
    endereco_id
)
VALUES
    (
        'cccccccc-cccc-cccc-cccc-ccccccccccc1',
        'Dono Sushi',
        'dono.sushi@foodlink.com',
        'dono.sushi',
        '123456',
        CURRENT_TIMESTAMP,
        '11111111-1111-1111-1111-111111111111',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1'
    ),
    (
        'cccccccc-cccc-cccc-cccc-ccccccccccc2',
        'Dono Burger',
        'dono.burger@foodlink.com',
        'dono.burger',
        '123456',
        CURRENT_TIMESTAMP,
        '11111111-1111-1111-1111-111111111111',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2'
    ),
    (
        'cccccccc-cccc-cccc-cccc-ccccccccccc3',
        'Dono Brasil',
        'dono.brasil@foodlink.com',
        'dono.brasil',
        '123456',
        CURRENT_TIMESTAMP,
        '11111111-1111-1111-1111-111111111111',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb3'
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO restaurantes (
    id,
    nome,
    cnpj,
    endereco_id,
    data_ultima_alteracao,
    tipo_cozinha,
    dono_restaurante_id
)
VALUES
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd1',
        'Foodlink Sushi',
        '23.456.789/0001-10',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb4',
        CURRENT_TIMESTAMP,
        'Japonesa',
        'cccccccc-cccc-cccc-cccc-ccccccccccc1'
    ),
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd2',
        'Foodlink Burger',
        '34.567.890/0001-20',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb5',
        CURRENT_TIMESTAMP,
        'Hamburgueria',
        'cccccccc-cccc-cccc-cccc-ccccccccccc2'
    ),
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd3',
        'Foodlink Brasil',
        '45.678.901/0001-30',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb6',
        CURRENT_TIMESTAMP,
        'Brasileira',
        'cccccccc-cccc-cccc-cccc-ccccccccccc3'
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO horarios_funcionamento_restaurante (
    id,
    restaurante_id,
    dias_semana,
    hora_abertura,
    hora_encerramento
)
VALUES
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee1',
        'dddddddd-dddd-dddd-dddd-ddddddddddd1',
        'TERCA',
        '11:30',
        '22:30'
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee2',
        'dddddddd-dddd-dddd-dddd-ddddddddddd1',
        'QUARTA',
        '11:30',
        '22:30'
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee3',
        'dddddddd-dddd-dddd-dddd-ddddddddddd1',
        'SEXTA',
        '11:30',
        '23:30'
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee4',
        'dddddddd-dddd-dddd-dddd-ddddddddddd2',
        'SEGUNDA',
        '12:00',
        '23:00'
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee5',
        'dddddddd-dddd-dddd-dddd-ddddddddddd2',
        'QUINT',
        '12:00',
        '23:00'
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee6',
        'dddddddd-dddd-dddd-dddd-ddddddddddd2',
        'SABADO',
        '12:00',
        '23:59'
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee7',
        'dddddddd-dddd-dddd-dddd-ddddddddddd3',
        'SEGUNDA',
        '11:00',
        '15:00'
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee8',
        'dddddddd-dddd-dddd-dddd-ddddddddddd3',
        'TERCA',
        '11:00',
        '15:00'
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee9',
        'dddddddd-dddd-dddd-dddd-ddddddddddd3',
        'DOMINGO',
        '11:00',
        '16:00'
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO itens_cardapio (
    id,
    nome,
    descricao,
    preco,
    restaurante_id,
    caminho_foto,
    disponivel_apenas_no_restaurante,
    data_ultima_alteracao
)
VALUES
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff1',
        'Combinado Sushi 20 Pecas',
        'Selecao de sushis e sashimis para uma pessoa.',
        79.90,
        'dddddddd-dddd-dddd-dddd-ddddddddddd1',
        'fotos/combinado-sushi-20.png',
        false,
        CURRENT_TIMESTAMP
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff2',
        'Temaki Salmao',
        'Temaki de salmao com cebolinha e cream cheese.',
        32.90,
        'dddddddd-dddd-dddd-dddd-ddddddddddd1',
        'fotos/temaki-salmao.png',
        false,
        CURRENT_TIMESTAMP
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff3',
        'Cha Verde Gelado',
        'Cha verde gelado da casa.',
        10.00,
        'dddddddd-dddd-dddd-dddd-ddddddddddd1',
        'fotos/cha-verde-gelado.png',
        true,
        CURRENT_TIMESTAMP
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff4',
        'Burger Classico',
        'Pao brioche, blend bovino, queijo, alface, tomate e molho da casa.',
        36.90,
        'dddddddd-dddd-dddd-dddd-ddddddddddd2',
        'fotos/burger-classico.png',
        false,
        CURRENT_TIMESTAMP
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff5',
        'Burger Bacon',
        'Pao brioche, blend bovino, queijo cheddar, bacon e cebola caramelizada.',
        42.90,
        'dddddddd-dddd-dddd-dddd-ddddddddddd2',
        'fotos/burger-bacon.png',
        false,
        CURRENT_TIMESTAMP
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff6',
        'Batata Rustica',
        'Batata rustica com alecrim e maionese temperada.',
        24.90,
        'dddddddd-dddd-dddd-dddd-ddddddddddd2',
        'fotos/batata-rustica.png',
        true,
        CURRENT_TIMESTAMP
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff7',
        'Feijoada Individual',
        'Feijoada com arroz, couve, farofa e laranja.',
        48.90,
        'dddddddd-dddd-dddd-dddd-ddddddddddd3',
        'fotos/feijoada-individual.png',
        false,
        CURRENT_TIMESTAMP
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff8',
        'Bife Acebolado',
        'Bife acebolado com arroz, feijao, fritas e salada.',
        39.90,
        'dddddddd-dddd-dddd-dddd-ddddddddddd3',
        'fotos/bife-acebolado.png',
        false,
        CURRENT_TIMESTAMP
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff9',
        'Pudim de Leite',
        'Pudim de leite condensado com calda de caramelo.',
        14.90,
        'dddddddd-dddd-dddd-dddd-ddddddddddd3',
        'fotos/pudim-de-leite.png',
        true,
        CURRENT_TIMESTAMP
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO enderecos (
    id,
    logradouro,
    numero,
    complemento,
    bairro,
    cidade,
    uf,
    cep,
    data_ultima_alteracao
)
VALUES
    (
        '33333333-3333-3333-3333-333333333333',
        'Avenida Paulista',
        '1000',
        'Conjunto 101',
        'Bela Vista',
        'Sao Paulo',
        'SP',
        '01310-100',
        CURRENT_TIMESTAMP
    ),
    (
        '44444444-4444-4444-4444-444444444444',
        'Rua Augusta',
        '500',
        'Apartamento 12',
        'Consolacao',
        'Sao Paulo',
        'SP',
        '01304-000',
        CURRENT_TIMESTAMP
    ),
    (
        '55555555-5555-5555-5555-555555555555',
        'Rua dos Pinheiros',
        '250',
        'Loja 1',
        'Pinheiros',
        'Sao Paulo',
        'SP',
        '05422-000',
        CURRENT_TIMESTAMP
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (
    id,
    nome,
    email,
    login,
    senha,
    data_ultima_alteracao,
    tipo_usuario_id,
    endereco_id
)
VALUES
    (
        '66666666-6666-6666-6666-666666666666',
        'Dono Foodlink',
        'dono@foodlink.com',
        'dono.foodlink',
        '123456',
        CURRENT_TIMESTAMP,
        '11111111-1111-1111-1111-111111111111',
        '33333333-3333-3333-3333-333333333333'
    ),
    (
        '77777777-7777-7777-7777-777777777777',
        'Cliente Foodlink',
        'cliente@foodlink.com',
        'cliente.foodlink',
        '123456',
        CURRENT_TIMESTAMP,
        '22222222-2222-2222-2222-222222222222',
        '44444444-4444-4444-4444-444444444444'
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO restaurantes (
    id,
    nome,
    cnpj,
    endereco_id,
    data_ultima_alteracao,
    tipo_cozinha,
    dono_restaurante_id
)
VALUES
    (
        '88888888-8888-8888-8888-888888888888',
        'Foodlink Pizzaria',
        '12.345.678/0001-90',
        '55555555-5555-5555-5555-555555555555',
        CURRENT_TIMESTAMP,
        'Italiana',
        '66666666-6666-6666-6666-666666666666'
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO horarios_funcionamento_restaurante (
    id,
    restaurante_id,
    dias_semana,
    hora_abertura,
    hora_encerramento
)
VALUES
    (
        '99999999-9999-9999-9999-999999999991',
        '88888888-8888-8888-8888-888888888888',
        'SEGUNDA',
        '18:00',
        '23:00'
    ),
    (
        '99999999-9999-9999-9999-999999999992',
        '88888888-8888-8888-8888-888888888888',
        'TERCA',
        '18:00',
        '23:00'
    ),
    (
        '99999999-9999-9999-9999-999999999993',
        '88888888-8888-8888-8888-888888888888',
        'QUARTA',
        '18:00',
        '23:00'
    ),
    (
        '99999999-9999-9999-9999-999999999994',
        '88888888-8888-8888-8888-888888888888',
        'QUINT',
        '18:00',
        '23:00'
    ),
    (
        '99999999-9999-9999-9999-999999999995',
        '88888888-8888-8888-8888-888888888888',
        'SEXTA',
        '18:00',
        '23:59'
    )
ON CONFLICT (id) DO NOTHING;

INSERT INTO itens_cardapio (
    id,
    nome,
    descricao,
    preco,
    restaurante_id,
    caminho_foto,
    disponivel_apenas_no_restaurante,
    data_ultima_alteracao
)
VALUES
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
        'Pizza Margherita',
        'Pizza com molho de tomate, mussarela e manjericao.',
        49.90,
        '88888888-8888-8888-8888-888888888888',
        'fotos/pizza-margherita.png',
        false,
        CURRENT_TIMESTAMP
    ),
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
        'Pizza Calabresa',
        'Pizza com calabresa, cebola, mussarela e oregano.',
        54.90,
        '88888888-8888-8888-8888-888888888888',
        'fotos/pizza-calabresa.png',
        false,
        CURRENT_TIMESTAMP
    ),
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3',
        'Refrigerante Lata',
        'Refrigerante em lata 350ml.',
        8.00,
        '88888888-8888-8888-8888-888888888888',
        'fotos/refrigerante-lata.png',
        true,
        CURRENT_TIMESTAMP
    )
ON CONFLICT (id) DO NOTHING;
