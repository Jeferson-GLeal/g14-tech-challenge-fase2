import type { User, UserType } from '../../types';

type UsersTableProps = {
  onDelete: (user: User) => void;
  onEdit: (user: User) => void;
  types: UserType[];
  users: User[];
};

export function UsersTable({ onDelete, onEdit, types, users }: UsersTableProps) {
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
