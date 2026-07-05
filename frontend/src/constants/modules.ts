import type { ModuleId } from '../types';

export const modules: Array<{ id: ModuleId; label: string; hint: string }> = [
  { id: 'users', label: 'Usuarios', hint: 'Clientes e donos' },
  { id: 'restaurants', label: 'Restaurantes', hint: 'Operacao e endereco' },
  { id: 'menuItems', label: 'Cardapio', hint: 'Produtos por restaurante' },
  { id: 'userTypes', label: 'Tipos', hint: 'Perfis do sistema' },
];

export const days = ['DOMINGO', 'SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO'];
