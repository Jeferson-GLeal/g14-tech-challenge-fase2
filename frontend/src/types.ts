export type ModuleId = 'users' | 'userTypes' | 'restaurants' | 'menuItems';

export type Mode = 'create' | 'edit';

export type Address = {
  street: string;
  number: string;
  complement: string;
  district: string;
  city: string;
  state: string;
  zipCode: string;
};

export type UserType = {
  id: string;
  name: string;
  code: 'DONO_RESTAURANTE' | 'CLIENTE';
};

export type User = {
  id: string;
  name: string;
  email: string;
  login: string;
  userTypeId: string;
  address: Address;
  lastUpdatedAt?: string;
};

export type WorkingPeriod = {
  day: string[];
  openTime: string;
  closeTime: string;
};

export type Restaurant = {
  id: string;
  name: string;
  cnpj: string;
  type: string;
  ownerId: string;
  ownerName: string;
  address: Address;
  period: Array<{ day: string; openTime: string; closeTime: string }>;
};

export type MenuItem = {
  id: string;
  name: string;
  description: string;
  price: number;
  restaurantId: string;
  photoPath: string;
  availableOnlyAtRestaurant: boolean;
};

export type UserForm = Omit<User, 'id' | 'lastUpdatedAt'> & { password: string };
export type RestaurantForm = Omit<Restaurant, 'id' | 'ownerName' | 'period'> & { period: WorkingPeriod };
export type MenuItemForm = Omit<MenuItem, 'id' | 'restaurantId'>;
