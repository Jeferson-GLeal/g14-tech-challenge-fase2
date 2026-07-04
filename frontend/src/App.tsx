import { FormEvent, useEffect, useMemo, useState } from 'react';
import { ConfirmModal } from './components/common/ConfirmModal';
import { Toast, type ToastMessage } from './components/common/Toast';
import { Metric } from './components/layout/Metric';
import { Sidebar } from './components/layout/Sidebar';
import { modules } from './constants/modules';
import { MenuItemFormView } from './features/menu-items/MenuItemFormView';
import { MenuItemsTable } from './features/menu-items/MenuItemsTable';
import { RestaurantFormView } from './features/restaurants/RestaurantFormView';
import { RestaurantsTable } from './features/restaurants/RestaurantsTable';
import { UserTypeFormView } from './features/user-types/UserTypeFormView';
import { UserTypesTable } from './features/user-types/UserTypesTable';
import { UserFormView } from './features/users/UserFormView';
import { UsersTable } from './features/users/UsersTable';
import { api } from './services/api';
import type { MenuItem, MenuItemForm, Mode, ModuleId, Restaurant, RestaurantForm, User, UserForm, UserType } from './types';
import {
  createMenuItemForm,
  createRestaurantForm,
  createUserForm,
  emptyAddress,
  formTitle,
  uniqueSuffix,
} from './utils/forms';

type DeleteRequest = {
  afterDelete: () => Promise<void>;
  label: string;
  path: string;
};

function getRestaurantOwnerUsers(users: User[], userTypes: UserType[]) {
  const ownerTypeIds = new Set(
    userTypes
      .filter((type) => type.code === 'DONO_RESTAURANTE')
      .map((type) => type.id),
  );

  return ownerTypeIds.size > 0
    ? users.filter((user) => ownerTypeIds.has(user.userTypeId))
    : users;
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
  const [toast, setToast] = useState<ToastMessage>();
  const [deleteRequest, setDeleteRequest] = useState<DeleteRequest>();
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
    return getRestaurantOwnerUsers(users, userTypes);
  }, [userTypes, users]);

  const selectedRestaurant = restaurants.find((restaurant) => restaurant.id === selectedRestaurantId);
  const moduleTitle = modules.find((module) => module.id === activeModule)?.label ?? '';

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

  useEffect(() => {
    if (ownerUsers.length === 0) return;
    if (ownerUsers.some((owner) => owner.id === restaurantForm.ownerId)) return;

    setRestaurantForm((currentForm) => ({
      ...currentForm,
      ownerId: ownerUsers[0].id,
    }));
  }, [ownerUsers, restaurantForm.ownerId]);

  useEffect(() => {
    if (!toast) return undefined;

    const timeout = window.setTimeout(() => {
      setToast(undefined);
    }, 4200);

    return () => window.clearTimeout(timeout);
  }, [toast]);

  function showToast(kind: ToastMessage['kind'], message: string) {
    setToast({ id: Date.now(), kind, message });
  }

  async function loadBaseData() {
    setLoading(true);
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
      setRestaurantForm(createRestaurantForm(getRestaurantOwnerUsers(userList, typeList)[0]?.id ?? ''));
    } catch (loadError) {
      showToast('danger', loadError instanceof Error ? loadError.message : 'Nao foi possivel carregar os dados.');
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
      showToast('danger', loadError instanceof Error ? loadError.message : 'Nao foi possivel carregar o cardapio.');
    }
  }

  function resetForm(moduleId = activeModule) {
    setMode('create');
    setEditingId('');

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
      showToast('success', 'Tipo de usuario salvo.');
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
      showToast('success', 'Usuario salvo.');
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
      showToast('success', 'Restaurante salvo.');
    });
  }

  async function submitMenuItem(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedRestaurantId) {
      showToast('danger', 'Selecione um restaurante para gerenciar o cardapio.');
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
      showToast('success', 'Item de cardapio salvo.');
    });
  }

  async function save(action: () => Promise<void>) {
    setSaving(true);
    try {
      await action();
    } catch (saveError) {
      showToast('danger', saveError instanceof Error ? saveError.message : 'Nao foi possivel salvar.');
    } finally {
      setSaving(false);
    }
  }

  function remove(path: string, afterDelete: () => Promise<void>, label: string) {
    setDeleteRequest({ afterDelete, label, path });
  }

  async function confirmDelete() {
    if (!deleteRequest) return;

    await save(async () => {
      await api<void>(deleteRequest.path, { method: 'DELETE' });
      await deleteRequest.afterDelete();
      resetForm();
      setDeleteRequest(undefined);
      showToast('success', 'Registro removido.');
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

  return (
    <main className="app-shell">
      <Sidebar activeModule={activeModule} onOpenModule={openModule} />

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

      <Toast toast={toast} onClose={() => setToast(undefined)} />
      <ConfirmModal
        confirmLabel="Remover"
        description={`Esta acao remove "${deleteRequest?.label ?? ''}" e nao pode ser desfeita.`}
        loading={saving}
        onCancel={() => setDeleteRequest(undefined)}
        onConfirm={() => void confirmDelete()}
        open={Boolean(deleteRequest)}
        title="Remover registro?"
      />
    </main>
  );
}
