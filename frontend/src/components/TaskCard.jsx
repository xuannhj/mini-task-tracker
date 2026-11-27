import { useState } from 'react';
import { Calendar, Loader2, Trash2 } from 'lucide-react';

const PRIORITY_BADGE = {
  HIGH: {
    label: 'Ưu tiên cao',
    className: 'bg-rose-50 text-rose-700 border-rose-200/80',
    dot: 'bg-rose-500',
  },
  MEDIUM: {
    label: 'Trung bình',
    className: 'bg-amber-50 text-amber-800 border-amber-200/80',
    dot: 'bg-amber-500',
  },
  LOW: {
    label: 'Thấp',
    className: 'bg-emerald-50 text-emerald-700 border-emerald-200/80',
    dot: 'bg-emerald-500',
  },
};

const STATUS_OPTIONS = [
  {
    value: 'TODO',
    label: 'Cần làm',
    activeClass: 'bg-slate-800 text-white border-slate-800',
  },
  {
    value: 'IN_PROGRESS',
    label: 'Đang làm',
    activeClass: 'bg-amber-600 text-white border-amber-600',
  },
  {
    value: 'DONE',
    label: 'Hoàn thành',
    activeClass: 'bg-teal-700 text-white border-teal-700',
  },
];

function formatDateTime(isoString) {
  if (!isoString) return '';
  const date = new Date(isoString);
  if (Number.isNaN(date.getTime())) return '';
  return new Intl.DateTimeFormat('vi-VN', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(date);
}

export default function TaskCard({ task, onStatusChange, onDelete }) {
  const [busy, setBusy] = useState(false);
  const [confirmingDelete, setConfirmingDelete] = useState(false);

  const priorityMeta = PRIORITY_BADGE[task.priority] || PRIORITY_BADGE.MEDIUM;
  const isDone = task.status === 'DONE';

  const handleStatusClick = async (newStatus) => {
    if (newStatus === task.status || busy) return;
    setBusy(true);
    try {
      await onStatusChange(task.id, newStatus);
    } finally {
      setBusy(false);
    }
  };

  const handleDeleteConfirm = async () => {
    setBusy(true);
    try {
      await onDelete(task.id);
    } finally {
      setBusy(false);
      setConfirmingDelete(false);
    }
  };

  return (
    <article className="group rounded-2xl border border-stone-200/90 bg-white p-4 shadow-xs transition hover:border-stone-300">
      <div className="flex flex-wrap items-center justify-between gap-2">
        <div className="flex items-center gap-2">
          <span className="font-mono text-xs font-medium text-stone-400">
            #{task.id}
          </span>
          <span
            className={`inline-flex items-center gap-1.5 rounded-full border px-2.5 py-0.5 text-[11px] font-medium ${priorityMeta.className}`}
          >
            <span className={`h-1.5 w-1.5 rounded-full ${priorityMeta.dot}`} />
            {priorityMeta.label}
          </span>
        </div>

        <div className="flex items-center gap-1.5 text-[11px] text-stone-400">
          <Calendar className="h-3.5 w-3.5" />
          <span>{formatDateTime(task.createdAt)}</span>
        </div>
      </div>

      <div className="mt-2.5">
        <h3
          className={`text-sm font-semibold leading-snug ${
            isDone ? 'text-stone-400 line-through' : 'text-stone-900'
          }`}
        >
          {task.title}
        </h3>
        {task.description && (
          <p className="mt-1 text-xs leading-relaxed text-stone-500">
            {task.description}
          </p>
        )}
      </div>

      <div className="mt-4 flex flex-wrap items-center justify-between gap-3 border-t border-stone-100 pt-3">
        <div
          className="inline-flex flex-wrap items-center gap-1.5"
          role="group"
          aria-label="Chuyển trạng thái"
        >
          {STATUS_OPTIONS.map((option) => {
            const active = task.status === option.value;
            return (
              <button
                key={option.value}
                type="button"
                disabled={busy}
                onClick={() => handleStatusClick(option.value)}
                className={`rounded-lg border px-2.5 py-1 text-xs font-medium transition cursor-pointer disabled:opacity-60 ${
                  active
                    ? option.activeClass
                    : 'border-stone-200 bg-stone-50/70 text-stone-600 hover:border-stone-300 hover:bg-stone-100'
                }`}
              >
                {option.label}
              </button>
            );
          })}
        </div>

        <div className="flex items-center gap-1.5">
          {busy && <Loader2 className="h-4 w-4 animate-spin text-stone-400" />}

          {confirmingDelete ? (
            <div className="flex items-center gap-1.5">
              <button
                type="button"
                disabled={busy}
                onClick={handleDeleteConfirm}
                className="rounded-lg bg-rose-600 px-2.5 py-1 text-xs font-medium text-white transition hover:bg-rose-700 cursor-pointer"
              >
                Xóa luôn
              </button>
              <button
                type="button"
                disabled={busy}
                onClick={() => setConfirmingDelete(false)}
                className="rounded-lg border border-stone-200 bg-white px-2.5 py-1 text-xs font-medium text-stone-600 transition hover:bg-stone-50 cursor-pointer"
              >
                Hủy
              </button>
            </div>
          ) : (
            <button
              type="button"
              disabled={busy}
              onClick={() => setConfirmingDelete(true)}
              aria-label={`Xóa công việc ${task.title}`}
              className="inline-flex items-center gap-1 rounded-lg px-2 py-1 text-xs font-medium text-stone-400 transition hover:bg-rose-50 hover:text-rose-600 cursor-pointer"
            >
              <Trash2 className="h-3.5 w-3.5" />
              <span>Xóa</span>
            </button>
          )}
        </div>
      </div>
    </article>
  );
}
