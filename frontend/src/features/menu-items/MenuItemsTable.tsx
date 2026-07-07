import type { MenuItem, Restaurant } from '../../types';
import { asMoney } from '../../utils/format';

type MenuItemsTableProps = {
  items: MenuItem[];
  onDelete: (item: MenuItem) => void;
  onEdit: (item: MenuItem) => void;
  restaurants: Restaurant[];
  selectedRestaurantId: string;
  setSelectedRestaurantId: (restaurantId: string) => void;
};

export function MenuItemsTable({
  items,
  onDelete,
  onEdit,
  restaurants,
  selectedRestaurantId,
  setSelectedRestaurantId,
}: MenuItemsTableProps) {
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
