import { useState } from 'react';
import { AlertCircle, Loader2, Plus } from 'lucide-react';

const PRIORITIES = [
  {
    value: 'LOW',
    label: 'Thấp',
    dot: 'bg-emerald-500',
    active: 'border-emerald-300 bg-emerald-50 text-emerald-800',
  },
  {
    value: 'MEDIUM',
    label: 'Trung bình',
    dot: 'bg-amber-500',
    active: 'border-amber-300 bg-amber-50 text-amber-800',
  },
  {
    value: 'HIGH',
    label: 'Cao',
    dot: 'bg-rose-500',
    active: 'border-rose-300 bg-rose-50 text-rose-800',
  },
];

export default function TaskForm({ onCreateTask }) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState('MEDIUM');
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setFormError('');
    setSubmitting(true);

    try {
      await onCreateTask({
        title: title.trim(),
        description: description.trim(),
        priority,
      });
      setTitle('');
      setDescription('');
      setPriority('MEDIUM');
    } catch (errMessage) {
      setFormError(
        typeof errMessage === 'string'
          ? errMessage
          : 'Không thể tạo công việc lúc này.'
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="rounded-2xl border border-stone-200/90 bg-white p-5 shadow-xs"
    >
      <h2 className="mb-4 text-base font-semibold text-stone-900">
        Thêm công việc mới
      </h2>

      {formError && (
        <div
          role="alert"
          className="mb-4 flex items-start gap-2 rounded-xl border border-rose-200 bg-rose-50 px-3.5 py-2.5 text-xs font-medium text-rose-700"
        >
          <AlertCircle className="mt-0.5 h-4 w-4 shrink-0" />
          <span>{formError}</span>
        </div>
      )}

      <div className="space-y-3.5">
        <div>
          <label
            htmlFor="task-title"
            className="mb-1.5 block text-xs font-medium text-stone-700"
          >
            Tiêu đề <span className="text-rose-500">*</span>
          </label>
          <input
            id="task-title"
            type="text"
            value={title}
            onChange={(e) => {
              setTitle(e.target.value);
              if (formError) setFormError('');
            }}
            placeholder="Nhập tiêu đề..."
            className="w-full rounded-xl border border-stone-200 bg-stone-50/50 px-3 py-2 text-sm text-stone-900 placeholder:text-stone-400 transition focus:border-teal-600 focus:bg-white focus:outline-none focus:ring-3 focus:ring-teal-600/10"
          />
        </div>

        <div>
          <label
            htmlFor="task-description"
            className="mb-1.5 block text-xs font-medium text-stone-700"
          >
            Mô tả
          </label>
          <textarea
            id="task-description"
            rows={2}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Ghi chú (không bắt buộc)..."
            className="w-full resize-none rounded-xl border border-stone-200 bg-stone-50/50 px-3 py-2 text-sm text-stone-900 placeholder:text-stone-400 transition focus:border-teal-600 focus:bg-white focus:outline-none focus:ring-3 focus:ring-teal-600/10"
          />
        </div>

        <div>
          <span className="mb-1.5 block text-xs font-medium text-stone-700">
            Mức độ ưu tiên
          </span>
          <div className="grid grid-cols-3 gap-1.5">
            {PRIORITIES.map((item) => {
              const isSelected = priority === item.value;
              return (
                <button
                  key={item.value}
                  type="button"
                  onClick={() => setPriority(item.value)}
                  className={`flex items-center justify-center gap-1.5 whitespace-nowrap rounded-xl border px-2 py-2 text-xs font-medium transition cursor-pointer ${
                    isSelected
                      ? item.active
                      : 'border-stone-200 bg-white text-stone-600 hover:bg-stone-50'
                  }`}
                >
                  <span className={`h-2 w-2 shrink-0 rounded-full ${item.dot}`} />
                  {item.label}
                </button>
              );
            })}
          </div>
        </div>

        <button
          type="submit"
          disabled={submitting}
          className="flex w-full items-center justify-center gap-2 rounded-xl bg-stone-900 px-4 py-2.5 text-sm font-medium text-white shadow-xs transition hover:bg-stone-800 disabled:cursor-not-allowed disabled:opacity-60 cursor-pointer"
        >
          {submitting ? (
            <>
              <Loader2 className="h-4 w-4 animate-spin" />
              Đang lưu...
            </>
          ) : (
            <>
              <Plus className="h-4 w-4" />
              Tạo công việc
            </>
          )}
        </button>
      </div>
    </form>
  );
}
