import type { UserType } from '../../types';

type UserTypesTableProps = {
  onDelete: (type: UserType) => void;
  onEdit: (type: UserType) => void;
  types: UserType[];
};

export function UserTypesTable({ onDelete, onEdit, types }: UserTypesTableProps) {
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
