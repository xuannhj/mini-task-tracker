import { CheckCircle2, Circle, Clock3, Layers } from 'lucide-react';

export default function StatsBar({ tasks }) {
  const total = tasks.length;
  const todoCount = tasks.filter((t) => t.status === 'TODO').length;
  const inProgressCount = tasks.filter((t) => t.status === 'IN_PROGRESS').length;
  const doneCount = tasks.filter((t) => t.status === 'DONE').length;

  const items = [
    {
      label: 'Tổng công việc',
      value: total,
      icon: Layers,
      iconClass: 'text-stone-600 bg-stone-100',
    },
    {
      label: 'Chờ xử lý',
      value: todoCount,
      icon: Circle,
      iconClass: 'text-slate-600 bg-slate-100',
    },
    {
      label: 'Đang thực hiện',
      value: inProgressCount,
      icon: Clock3,
      iconClass: 'text-amber-700 bg-amber-50',
    },
    {
      label: 'Đã hoàn thành',
      value: doneCount,
      icon: CheckCircle2,
      iconClass: 'text-teal-700 bg-teal-50',
    },
  ];

  return (
    <section
      aria-label="Thống kê tiến độ"
      className="grid grid-cols-2 gap-3 sm:grid-cols-4"
    >
      {items.map((item) => {
        const Icon = item.icon;
        return (
          <div
            key={item.label}
            className="rounded-2xl border border-stone-200/90 bg-white p-4 shadow-xs transition-colors hover:border-stone-300"
          >
            <div className="flex items-center justify-between">
              <span className="text-xs font-medium text-stone-500">
                {item.label}
              </span>
              <span className={`rounded-lg p-1.5 ${item.iconClass}`}>
                <Icon className="h-4 w-4" />
              </span>
            </div>
            <p className="mt-3 text-2xl font-bold tracking-tight text-stone-900">
              {item.value}
            </p>
          </div>
        );
      })}
    </section>
  );
}
