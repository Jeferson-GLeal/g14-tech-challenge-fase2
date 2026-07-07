import type { Address, MenuItemForm, Mode, ModuleId, RestaurantForm, UserForm } from '../types';

export const emptyAddress: Address = {
  street: '',
  number: '',
  complement: '',
  district: '',
  city: 'Sao Paulo',
  state: 'SP',
  zipCode: '',
};

export const defaultAddress: Address = {
  street: 'Rua Teste',
  number: '123',
  complement: 'Sala 1',
  district: 'Centro',
  city: 'Sao Paulo',
  state: 'SP',
  zipCode: '01000-000',
};

export function uniqueSuffix() {
  return Date.now().toString().slice(-6);
}

export function createUserForm(userTypeId = ''): UserForm {
  const suffix = uniqueSuffix();
  return {
    name: `Usuario ${suffix}`,
    email: `usuario.${suffix}@foodlink.com`,
    login: `usuario.${suffix}`,
    password: '123456',
    userTypeId,
    address: defaultAddress,
  };
}

export function createRestaurantForm(ownerId = ''): RestaurantForm {
  const suffix = uniqueSuffix();
  return {
    name: `Restaurante ${suffix}`,
    cnpj: `98.765.${suffix.slice(0, 3)}/0001-11`,
    type: 'Comida Rapida',
    ownerId,
    address: defaultAddress,
    period: {
      day: ['SEGUNDA', 'TERCA', 'QUARTA'],
      openTime: '10:00',
      closeTime: '22:00',
    },
  };
}

export function createMenuItemForm(): MenuItemForm {
  const suffix = uniqueSuffix();
  return {
    name: `Item ${suffix}`,
    description: 'Item criado pelo painel Foodlink.',
    price: 19.9,
    photoPath: 'fotos/item-teste.png',
    availableOnlyAtRestaurant: false,
  };
}

export function field<T extends object, K extends keyof T>(state: T, key: K, value: T[K]) {
  return { ...state, [key]: value };
}

export function formTitle(moduleId: ModuleId, mode: Mode) {
  const action = mode === 'edit' ? 'Editar' : 'Cadastrar';
  const labels: Record<ModuleId, string> = {
    users: 'usuario',
    restaurants: 'restaurante',
    menuItems: 'item',
    userTypes: 'tipo',
  };
  return `${action} ${labels[moduleId]}`;
}
