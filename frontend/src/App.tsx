import { FormEvent, useEffect, useMemo, useState } from 'react';

type ModuleId = 'users' | 'userTypes' | 'restaurants' | 'menuItems';
type Mode = 'create' | 'edit';

type ApiErrorBody = {
  message?: string;
  error?: string;
  details?: string;
};

type Address = {
  street: string;
  number: string;
  complement: string;
  district: string;
  city: string;
  state: string;
  zipCode: string;
};

type UserType = {
  id: string;
  name: string;
  code: 'DONO_RESTAURANTE' | 'CLIENTE';
};

type User = {
  id: string;
  name: string;
  email: string;
  login: string;
  userTypeId: string;
  address: Address;
  lastUpdatedAt?: string;
};

type WorkingPeriod = {
  day: string[];
  openTime: string;
  closeTime: string;
};

type Restaurant = {
  id: string;
  name: string;
  cnpj: string;
  type: string;
  ownerId: string;
  ownerName: string;
  address: Address;
  period: Array<{ day: string; openTime: string; closeTime: string }>;
};

type MenuItem = {
  id: string;
  name: string;
  description: string;
  price: number;
  restaurantId: string;
  photoPath: string;
  availableOnlyAtRestaurant: boolean;
};

type UserForm = Omit<User, 'id' | 'lastUpdatedAt'> & { password: string };
type RestaurantForm = Omit<Restaurant, 'id' | 'ownerName' | 'period'> & { period: WorkingPeriod };
type MenuItemForm = Omit<MenuItem, 'id' | 'restaurantId'>;

const modules: Array<{ id: ModuleId; label: string; hint: string }> = [
  { id: 'users', label: 'Usuarios', hint: 'Clientes e donos' },
  { id: 'restaurants', label: 'Restaurantes', hint: 'Operacao e endereco' },
  { id: 'menuItems', label: 'Cardapio', hint: 'Produtos por restaurante' },
  { id: 'userTypes', label: 'Tipos', hint: 'Perfis do sistema' },
];

const days = ['DOMINGO', 'SEGUNDA', 'TERCA', 'QUARTA', 'QUINT', 'SEXTA', 'SABADO'];

const emptyAddress: Address = {
  street: '',
  number: '',
  complement: '',
  district: '',
  city: 'Sao Paulo',
  state: 'SP',
  zipCode: '',
};

const defaultAddress: Address = {
  street: 'Rua Teste',
  number: '123',
  complement: 'Sala 1',
  district: 'Centro',
  city: 'Sao Paulo',
  state: 'SP',
  zipCode: '01000-000',
};

function uniqueSuffix() {
  return Date.now().toString().slice(-6);
}

function createUserForm(userTypeId = ''): UserForm {
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

function createRestaurantForm(ownerId = ''): RestaurantForm {
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

function createMenuItemForm(): MenuItemForm {
  const suffix = uniqueSuffix();
  return {
    name: `Item ${suffix}`,
    description: 'Item criado pelo painel Foodlink.',
    price: 19.9,
    photoPath: 'fotos/item-teste.png',
    availableOnlyAtRestaurant: false,
  };
}

async function api<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(path, {
    ...init,
    headers: {
      Accept: 'application/json',
      ...(init?.body ? { 'Content-Type': 'application/json' } : {}),
      ...init?.headers,
    },
  });

  if (!response.ok) {
    let message = `${response.status} ${response.statusText}`;
    try {
      const body = (await response.json()) as ApiErrorBody;
      message = body.message ?? body.error ?? body.details ?? message;
    } catch {
      message = (await response.text()) || message;
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

function asMoney(value: number) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
}

function field<T extends object, K extends keyof T>(state: T, key: K, value: T[K]) {
  return { ...state, [key]: value };
}

export function App() {
  const [activeModule, setActiveModule] = useState<ModuleId>('users');
  const [userTypes, setUserTypes] = useState<UserType[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [selectedRestaurantId, setSelectedRestaurantId] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [notice, setNotice] = useState('');
  const [error, setError] = useState('');
  const [mode, setMode] = useState<Mode>('create');
  const [editingId, setEditingId] = useState('');
  const [userTypeForm, setUserTypeForm] = useState<Omit<UserType, 'id'>>({
    name: 'Cliente Portal',
    code: 'CLIENTE',
  });
  const [userForm, setUserForm] = useState<UserForm>(createUserForm());
  const [restaurantForm, setRestaurantForm] = useState<RestaurantForm>(createRestaurantForm());
  const [menuItemForm, setMenuItemForm] = useState<MenuItemForm>(createMenuItemForm());

  const ownerUsers = useMemo(() => {
    const ownerType = userTypes.find((type) => type.code === 'DONO_RESTAURANTE');
    return ownerType ? users.filter((user) => user.userTypeId === ownerType.id) : users;
  }, [userTypes, users]);

  const selectedRestaurant = restaurants.find((restaurant) => restaurant.id === selectedRestaurantId);

  useEffect(() => {
    void loadBaseData();
  }, []);

  useEffect(() => {
    if (!selectedRestaurantId && restaurants.length > 0) {
      setSelectedRestaurantId(restaurants[0].id);
    }
  }, [restaurants, selectedRestaurantId]);

  useEffect(() => {
    if (selectedRestaurantId) {
      void loadMenuItems(selectedRestaurantId);
    }
  }, [selectedRestaurantId]);

  async function loadBaseData() {
    setLoading(true);
    setError('');
    try {
      const [typeList, userList, restaurantList] = await Promise.all([
        api<UserType[]>('/api/user-types'),
        api<User[]>('/api/users'),
        api<Restaurant[]>('/restaurants'),
      ]);
      setUserTypes(typeList);
      setUsers(userList);
      setRestaurants(restaurantList);
      setUserForm(createUserForm(typeList.find((type) => type.code === 'CLIENTE')?.id ?? typeList[0]?.id ?? ''));
      setRestaurantForm(createRestaurantForm(userList[0]?.id ?? ''));
    } catch (loadError) {
      setError(loadError instanceof Error ? loadError.message : 'Nao foi possivel carregar os dados.');
    } finally {
      setLoading(false);
    }
  }

  async function loadMenuItems(restaurantId = selectedRestaurantId) {
    if (!restaurantId) {
      setMenuItems([]);
      return;
    }

    try {
      setMenuItems(await api<MenuItem[]>(`/api/menu-items/${restaurantId}`));
    } catch (loadError) {
      setMenuItems([]);
      setError(loadError instanceof Error ? loadError.message : 'Nao foi possivel carregar o cardapio.');
    }
  }

  function resetForm(moduleId = activeModule) {
    setMode('create');
    setEditingId('');
    setError('');
    if (moduleId === 'userTypes') {
      setUserTypeForm({ name: `Perfil ${uniqueSuffix()}`, code: 'CLIENTE' });
    }
    if (moduleId === 'users') {
      setUserForm(createUserForm(userTypes.find((type) => type.code === 'CLIENTE')?.id ?? userTypes[0]?.id ?? ''));
    }
    if (moduleId === 'restaurants') {
      setRestaurantForm(createRestaurantForm(ownerUsers[0]?.id ?? users[0]?.id ?? ''));
    }
    if (moduleId === 'menuItems') {
      setMenuItemForm(createMenuItemForm());
    }
  }

  function openModule(moduleId: ModuleId) {
    setActiveModule(moduleId);
    setNotice('');
    resetForm(moduleId);
  }

  async function submitUserType(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await save(async () => {
      if (mode === 'edit') {
        await api<UserType>(`/api/user-types/${editingId}`, {
          method: 'PUT',
          body: JSON.stringify(userTypeForm),
        });
      } else {
        await api<UserType>('/api/user-types', {
          method: 'POST',
          body: JSON.stringify(userTypeForm),
        });
      }
      await loadBaseData();
      resetForm('userTypes');
      setNotice('Tipo de usuario salvo.');
    });
  }

  async function submitUser(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await save(async () => {
      const payload = mode === 'edit'
        ? {
          name: userForm.name,
          email: userForm.email,
          login: userForm.login,
          userTypeId: userForm.userTypeId,
          address: userForm.address,
        }
        : userForm;

      await api<User>(mode === 'edit' ? `/api/users/${editingId}` : '/api/users', {
        method: mode === 'edit' ? 'PUT' : 'POST',
        body: JSON.stringify(payload),
      });
      await loadBaseData();
      resetForm('users');
      setNotice('Usuario salvo.');
    });
  }

  async function submitRestaurant(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await save(async () => {
      await api<Restaurant>(mode === 'edit' ? `/restaurants/${editingId}` : '/restaurants', {
        method: mode === 'edit' ? 'PUT' : 'POST',
        body: JSON.stringify(restaurantForm),
      });
      await loadBaseData();
      resetForm('restaurants');
      setNotice('Restaurante salvo.');
    });
  }

  async function submitMenuItem(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedRestaurantId) {
      setError('Selecione um restaurante para gerenciar o cardapio.');
      return;
    }

    await save(async () => {
      await api<MenuItem>(
        mode === 'edit'
          ? `/api/menu-items/${selectedRestaurantId}/${editingId}`
          : `/api/menu-items/${selectedRestaurantId}`,
        {
          method: mode === 'edit' ? 'PUT' : 'POST',
          body: JSON.stringify(menuItemForm),
        },
      );
      await loadMenuItems(selectedRestaurantId);
      resetForm('menuItems');
      setNotice('Item de cardapio salvo.');
    });
  }

  async function save(action: () => Promise<void>) {
    setSaving(true);
    setError('');
    setNotice('');
    try {
      await action();
    } catch (saveError) {
      setError(saveError instanceof Error ? saveError.message : 'Nao foi possivel salvar.');
    } finally {
      setSaving(false);
    }
  }

  async function remove(path: string, afterDelete: () => Promise<void>, label: string) {
    const confirmed = window.confirm(`Remover ${label}?`);
    if (!confirmed) return;

    await save(async () => {
      await api<void>(path, { method: 'DELETE' });
      await afterDelete();
      resetForm();
      setNotice('Registro removido.');
    });
  }

  function editUserType(type: UserType) {
    setMode('edit');
    setEditingId(type.id);
    setUserTypeForm({ name: type.name, code: type.code });
  }

  function editUser(user: User) {
    setMode('edit');
    setEditingId(user.id);
    setUserForm({
      name: user.name,
      email: user.email,
      login: user.login,
      password: '',
      userTypeId: user.userTypeId,
      address: user.address ?? emptyAddress,
    });
  }

  function editRestaurant(restaurant: Restaurant) {
    const firstPeriod = restaurant.period?.[0];
    setMode('edit');
    setEditingId(restaurant.id);
    setRestaurantForm({
      name: restaurant.name,
      cnpj: restaurant.cnpj,
      type: restaurant.type,
      ownerId: restaurant.ownerId,
      address: restaurant.address ?? emptyAddress,
      period: {
        day: restaurant.period?.map((period) => period.day) ?? [],
        openTime: firstPeriod?.openTime ?? '10:00',
        closeTime: firstPeriod?.closeTime ?? '22:00',
      },
    });
  }

  function editMenuItem(item: MenuItem) {
    setMode('edit');
    setEditingId(item.id);
    setMenuItemForm({
      name: item.name,
      description: item.description,
      price: item.price,
      photoPath: item.photoPath,
      availableOnlyAtRestaurant: item.availableOnlyAtRestaurant,
    });
  }

  const moduleTitle = modules.find((module) => module.id === activeModule)?.label ?? '';

  return (
    <main className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <span className="brand-mark">F</span>
          <div>
            <strong>Foodlink</strong>
            <small>Admin</small>
          </div>
        </div>

        <nav className="nav-list" aria-label="Modulos">
          {modules.map((module) => (
            <button
              className={activeModule === module.id ? 'nav-item active' : 'nav-item'}
              key={module.id}
              onClick={() => openModule(module.id)}
              type="button"
            >
              <span>{module.label}</span>
              <small>{module.hint}</small>
            </button>
          ))}
        </nav>

        <a className="swagger-link" href="/swagger-ui.html" rel="noreferrer" target="_blank">
          Abrir Swagger
        </a>
      </aside>

      <section className="content">
        <header className="page-header">
          <div>
            <p className="eyebrow">Painel operacional</p>
            <h1>{moduleTitle}</h1>
          </div>
          <button className="secondary-button" onClick={() => void loadBaseData()} type="button">
            Atualizar
          </button>
        </header>

        <section className="metrics">
          <Metric label="Usuarios" value={users.length} />
          <Metric label="Restaurantes" value={restaurants.length} />
          <Metric label="Itens ativos" value={menuItems.length} />
          <Metric label="Tipos" value={userTypes.length} />
        </section>

        {notice && <div className="notice success">{notice}</div>}
        {error && <div className="notice danger">{error}</div>}

        {loading ? (
          <div className="empty-state">Carregando dados...</div>
        ) : (
          <section className="module-grid">
            <section className="list-surface">
              {activeModule === 'users' && (
                <UsersTable
                  onDelete={(user) => remove(`/api/users/${user.id}`, loadBaseData, user.name)}
                  onEdit={editUser}
                  types={userTypes}
                  users={users}
                />
              )}

              {activeModule === 'restaurants' && (
                <RestaurantsTable
                  onDelete={(restaurant) => remove(`/restaurants/${restaurant.id}`, loadBaseData, restaurant.name)}
                  onEdit={editRestaurant}
                  restaurants={restaurants}
                />
              )}

              {activeModule === 'menuItems' && (
                <MenuItemsTable
                  items={menuItems}
                  onDelete={(item) => remove(
                    `/api/menu-items/${selectedRestaurantId}/${item.id}`,
                    () => loadMenuItems(selectedRestaurantId),
                    item.name,
                  )}
                  onEdit={editMenuItem}
                  restaurants={restaurants}
                  selectedRestaurantId={selectedRestaurantId}
                  setSelectedRestaurantId={(restaurantId) => {
                    setSelectedRestaurantId(restaurantId);
                    resetForm('menuItems');
                  }}
                />
              )}

              {activeModule === 'userTypes' && (
                <UserTypesTable
                  onDelete={(type) => remove(`/api/user-types/${type.id}`, loadBaseData, type.name)}
                  onEdit={editUserType}
                  types={userTypes}
                />
              )}
            </section>

            <aside className="form-surface">
              <div className="form-heading">
                <div>
                  <p className="eyebrow">{mode === 'edit' ? 'Edicao' : 'Cadastro'}</p>
                  <h2>{formTitle(activeModule, mode)}</h2>
                </div>
                <button className="ghost-button" onClick={() => resetForm()} type="button">
                  Novo
                </button>
              </div>

              {activeModule === 'users' && (
                <UserFormView
                  form={userForm}
                  mode={mode}
                  onChange={setUserForm}
                  onSubmit={submitUser}
                  saving={saving}
                  types={userTypes}
                />
              )}

              {activeModule === 'restaurants' && (
                <RestaurantFormView
                  form={restaurantForm}
                  onChange={setRestaurantForm}
                  onSubmit={submitRestaurant}
                  owners={ownerUsers}
                  saving={saving}
                />
              )}

              {activeModule === 'menuItems' && (
                <MenuItemFormView
                  disabled={!selectedRestaurant}
                  form={menuItemForm}
                  onChange={setMenuItemForm}
                  onSubmit={submitMenuItem}
                  saving={saving}
                  selectedRestaurant={selectedRestaurant}
                />
              )}

              {activeModule === 'userTypes' && (
                <UserTypeFormView
                  form={userTypeForm}
                  onChange={setUserTypeForm}
                  onSubmit={submitUserType}
                  saving={saving}
                />
              )}
            </aside>
          </section>
        )}
      </section>
    </main>
  );
}

function Metric({ label, value }: { label: string; value: number }) {
  return (
    <div className="metric">
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  );
}

function formTitle(moduleId: ModuleId, mode: Mode) {
  const action = mode === 'edit' ? 'Editar' : 'Cadastrar';
  const labels: Record<ModuleId, string> = {
    users: 'usuario',
    restaurants: 'restaurante',
    menuItems: 'item',
    userTypes: 'tipo',
  };
  return `${action} ${labels[moduleId]}`;
}

function UsersTable({
  onDelete,
  onEdit,
  types,
  users,
}: {
  onDelete: (user: User) => void;
  onEdit: (user: User) => void;
  types: UserType[];
  users: User[];
}) {
  return (
    <>
      <div className="section-title">
        <h2>Usuarios cadastrados</h2>
        <span>{users.length} registros</span>
      </div>
      <div className="table-scroll">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>Email</th>
              <th>Login</th>
              <th>Tipo</th>
              <th>Cidade</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td>{user.login}</td>
                <td>{types.find((type) => type.id === user.userTypeId)?.name ?? user.userTypeId}</td>
                <td>{user.address?.city ?? '-'}</td>
                <td className="actions">
                  <button onClick={() => onEdit(user)} type="button">Editar</button>
                  <button className="danger-button" onClick={() => onDelete(user)} type="button">Excluir</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

function RestaurantsTable({
  onDelete,
  onEdit,
  restaurants,
}: {
  onDelete: (restaurant: Restaurant) => void;
  onEdit: (restaurant: Restaurant) => void;
  restaurants: Restaurant[];
}) {
  return (
    <>
      <div className="section-title">
        <h2>Restaurantes</h2>
        <span>{restaurants.length} registros</span>
      </div>
      <div className="table-scroll">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>Cozinha</th>
              <th>CNPJ</th>
              <th>Dono</th>
              <th>Horario</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {restaurants.map((restaurant) => (
              <tr key={restaurant.id}>
                <td>{restaurant.name}</td>
                <td>{restaurant.type}</td>
                <td>{restaurant.cnpj}</td>
                <td>{restaurant.ownerName}</td>
                <td>{restaurant.period?.[0] ? `${restaurant.period[0].openTime} - ${restaurant.period[0].closeTime}` : '-'}</td>
                <td className="actions">
                  <button onClick={() => onEdit(restaurant)} type="button">Editar</button>
                  <button className="danger-button" onClick={() => onDelete(restaurant)} type="button">Excluir</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

function MenuItemsTable({
  items,
  onDelete,
  onEdit,
  restaurants,
  selectedRestaurantId,
  setSelectedRestaurantId,
}: {
  items: MenuItem[];
  onDelete: (item: MenuItem) => void;
  onEdit: (item: MenuItem) => void;
  restaurants: Restaurant[];
  selectedRestaurantId: string;
  setSelectedRestaurantId: (restaurantId: string) => void;
}) {
  return (
    <>
      <div className="section-title stacked">
        <div>
          <h2>Itens do cardapio</h2>
          <span>{items.length} registros</span>
        </div>
        <select value={selectedRestaurantId} onChange={(event) => setSelectedRestaurantId(event.target.value)}>
          {restaurants.map((restaurant) => (
            <option key={restaurant.id} value={restaurant.id}>{restaurant.name}</option>
          ))}
        </select>
      </div>
      <div className="table-scroll">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>Descricao</th>
              <th>Preco</th>
              <th>Consumo local</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {items.map((item) => (
              <tr key={item.id}>
                <td>{item.name}</td>
                <td>{item.description}</td>
                <td>{asMoney(item.price)}</td>
                <td>{item.availableOnlyAtRestaurant ? 'Sim' : 'Nao'}</td>
                <td className="actions">
                  <button onClick={() => onEdit(item)} type="button">Editar</button>
                  <button className="danger-button" onClick={() => onDelete(item)} type="button">Excluir</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

function UserTypesTable({
  onDelete,
  onEdit,
  types,
}: {
  onDelete: (type: UserType) => void;
  onEdit: (type: UserType) => void;
  types: UserType[];
}) {
  return (
    <>
      <div className="section-title">
        <h2>Tipos de usuario</h2>
        <span>{types.length} registros</span>
      </div>
      <div className="table-scroll">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>Codigo</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {types.map((type) => (
              <tr key={type.id}>
                <td>{type.name}</td>
                <td><span className="tag">{type.code}</span></td>
                <td className="actions">
                  <button onClick={() => onEdit(type)} type="button">Editar</button>
                  <button className="danger-button" onClick={() => onDelete(type)} type="button">Excluir</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

function UserFormView({
  form,
  mode,
  onChange,
  onSubmit,
  saving,
  types,
}: {
  form: UserForm;
  mode: Mode;
  onChange: (form: UserForm) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  saving: boolean;
  types: UserType[];
}) {
  return (
    <form className="entity-form" onSubmit={onSubmit}>
      <TextField label="Nome" value={form.name} onChange={(value) => onChange(field(form, 'name', value))} />
      <TextField label="Email" type="email" value={form.email} onChange={(value) => onChange(field(form, 'email', value))} />
      <TextField label="Login" value={form.login} onChange={(value) => onChange(field(form, 'login', value))} />
      {mode === 'create' && (
        <TextField label="Senha" type="password" value={form.password} onChange={(value) => onChange(field(form, 'password', value))} />
      )}
      <label className="input-field">
        <span>Tipo</span>
        <select value={form.userTypeId} onChange={(event) => onChange(field(form, 'userTypeId', event.target.value))}>
          {types.map((type) => (
            <option key={type.id} value={type.id}>{type.name}</option>
          ))}
        </select>
      </label>
      <AddressFields address={form.address} onChange={(address) => onChange(field(form, 'address', address))} />
      <SubmitButton saving={saving} />
    </form>
  );
}

function RestaurantFormView({
  form,
  onChange,
  onSubmit,
  owners,
  saving,
}: {
  form: RestaurantForm;
  onChange: (form: RestaurantForm) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  owners: User[];
  saving: boolean;
}) {
  return (
    <form className="entity-form" onSubmit={onSubmit}>
      <TextField label="Nome" value={form.name} onChange={(value) => onChange(field(form, 'name', value))} />
      <TextField label="CNPJ" value={form.cnpj} onChange={(value) => onChange(field(form, 'cnpj', value))} />
      <TextField label="Tipo de cozinha" value={form.type} onChange={(value) => onChange(field(form, 'type', value))} />
      <label className="input-field">
        <span>Dono</span>
        <select value={form.ownerId} onChange={(event) => onChange(field(form, 'ownerId', event.target.value))}>
          {owners.map((owner) => (
            <option key={owner.id} value={owner.id}>{owner.name}</option>
          ))}
        </select>
      </label>
      <AddressFields address={form.address} onChange={(address) => onChange(field(form, 'address', address))} />
      <fieldset className="fieldset">
        <legend>Funcionamento</legend>
        <div className="day-grid">
          {days.map((day) => (
            <label key={day}>
              <input
                checked={form.period.day.includes(day)}
                onChange={(event) => {
                  const nextDays = event.target.checked
                    ? [...form.period.day, day]
                    : form.period.day.filter((selectedDay) => selectedDay !== day);
                  onChange(field(form, 'period', { ...form.period, day: nextDays }));
                }}
                type="checkbox"
              />
              {day.slice(0, 3)}
            </label>
          ))}
        </div>
        <div className="two-columns">
          <TextField label="Abre" type="time" value={form.period.openTime} onChange={(value) => onChange(field(form, 'period', { ...form.period, openTime: value }))} />
          <TextField label="Fecha" type="time" value={form.period.closeTime} onChange={(value) => onChange(field(form, 'period', { ...form.period, closeTime: value }))} />
        </div>
      </fieldset>
      <SubmitButton saving={saving} />
    </form>
  );
}

function MenuItemFormView({
  disabled,
  form,
  onChange,
  onSubmit,
  saving,
  selectedRestaurant,
}: {
  disabled: boolean;
  form: MenuItemForm;
  onChange: (form: MenuItemForm) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  saving: boolean;
  selectedRestaurant?: Restaurant;
}) {
  return (
    <form className="entity-form" onSubmit={onSubmit}>
      <p className="context-line">{selectedRestaurant ? `Restaurante: ${selectedRestaurant.name}` : 'Selecione um restaurante.'}</p>
      <TextField disabled={disabled} label="Nome" value={form.name} onChange={(value) => onChange(field(form, 'name', value))} />
      <label className="input-field">
        <span>Descricao</span>
        <textarea disabled={disabled} value={form.description} onChange={(event) => onChange(field(form, 'description', event.target.value))} />
      </label>
      <TextField disabled={disabled} label="Preco" min="0" step="0.01" type="number" value={String(form.price)} onChange={(value) => onChange(field(form, 'price', Number(value)))} />
      <TextField disabled={disabled} label="Foto" value={form.photoPath} onChange={(value) => onChange(field(form, 'photoPath', value))} />
      <label className="checkbox-field">
        <input
          checked={form.availableOnlyAtRestaurant}
          disabled={disabled}
          onChange={(event) => onChange(field(form, 'availableOnlyAtRestaurant', event.target.checked))}
          type="checkbox"
        />
        Disponivel apenas no restaurante
      </label>
      <SubmitButton disabled={disabled} saving={saving} />
    </form>
  );
}

function UserTypeFormView({
  form,
  onChange,
  onSubmit,
  saving,
}: {
  form: Omit<UserType, 'id'>;
  onChange: (form: Omit<UserType, 'id'>) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  saving: boolean;
}) {
  return (
    <form className="entity-form" onSubmit={onSubmit}>
      <TextField label="Nome" value={form.name} onChange={(value) => onChange(field(form, 'name', value))} />
      <label className="input-field">
        <span>Codigo</span>
        <select value={form.code} onChange={(event) => onChange(field(form, 'code', event.target.value as UserType['code']))}>
          <option value="CLIENTE">CLIENTE</option>
          <option value="DONO_RESTAURANTE">DONO_RESTAURANTE</option>
        </select>
      </label>
      <SubmitButton saving={saving} />
    </form>
  );
}

function AddressFields({ address, onChange }: { address: Address; onChange: (address: Address) => void }) {
  return (
    <fieldset className="fieldset">
      <legend>Endereco</legend>
      <TextField label="Logradouro" value={address.street} onChange={(value) => onChange(field(address, 'street', value))} />
      <div className="two-columns">
        <TextField label="Numero" value={address.number} onChange={(value) => onChange(field(address, 'number', value))} />
        <TextField label="Complemento" value={address.complement ?? ''} onChange={(value) => onChange(field(address, 'complement', value))} />
      </div>
      <TextField label="Bairro" value={address.district} onChange={(value) => onChange(field(address, 'district', value))} />
      <div className="two-columns">
        <TextField label="Cidade" value={address.city} onChange={(value) => onChange(field(address, 'city', value))} />
        <TextField label="UF" maxLength={2} value={address.state} onChange={(value) => onChange(field(address, 'state', value.toUpperCase()))} />
      </div>
      <TextField label="CEP" value={address.zipCode} onChange={(value) => onChange(field(address, 'zipCode', value))} />
    </fieldset>
  );
}

function TextField({
  disabled,
  label,
  onChange,
  value,
  ...props
}: {
  disabled?: boolean;
  label: string;
  onChange: (value: string) => void;
  value: string;
} & Omit<React.InputHTMLAttributes<HTMLInputElement>, 'onChange' | 'value'>) {
  return (
    <label className="input-field">
      <span>{label}</span>
      <input disabled={disabled} value={value} onChange={(event) => onChange(event.target.value)} {...props} />
    </label>
  );
}

function SubmitButton({ disabled, saving }: { disabled?: boolean; saving: boolean }) {
  return (
    <button className="primary-button" disabled={disabled || saving} type="submit">
      {saving ? 'Salvando...' : 'Salvar'}
    </button>
  );
}
