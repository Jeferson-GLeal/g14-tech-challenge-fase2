import { modules } from '../../constants/modules';
import type { ModuleId } from '../../types';

type SidebarProps = {
  activeModule: ModuleId;
  onOpenModule: (moduleId: ModuleId) => void;
};

export function Sidebar({ activeModule, onOpenModule }: SidebarProps) {
  return (
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
            onClick={() => onOpenModule(module.id)}
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
  );
}
