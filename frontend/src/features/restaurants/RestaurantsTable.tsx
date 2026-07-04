import type { Restaurant } from '../../types';

type RestaurantsTableProps = {
  onDelete: (restaurant: Restaurant) => void;
  onEdit: (restaurant: Restaurant) => void;
  restaurants: Restaurant[];
};

export function RestaurantsTable({ onDelete, onEdit, restaurants }: RestaurantsTableProps) {
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
