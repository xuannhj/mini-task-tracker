import { useCallback, useEffect, useState } from 'react';
import {
  AlertTriangle,
  CheckCircle2,
  ClipboardList,
  RefreshCw,
} from 'lucide-react';
import {
  createTask,
  deleteTask,
  extractErrorMessage,
  getTasks,
  updateTaskStatus,
} from './api/taskApi';
import StatsBar from './components/StatsBar';
import TaskForm from './components/TaskForm';
import TaskCard from './components/TaskCard';

const FILTER_TABS = [
  { value: null, label: 'Tất cả' },
  { value: 'TODO', label: 'Cần làm' },
  { value: 'IN_PROGRESS', label: 'Đang làm' },
  { value: 'DONE', label: 'Hoàn thành' },
];

export default function App() {
  const [tasks, setTasks] = useState([]);
  const [allTasks, setAllTasks] = useState([]);
  const [filterStatus, setFilterStatus] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');

  const showNotice = (text) => {
    setNotice(text);
    window.setTimeout(() => {
      setNotice((current) => (current === text ? '' : current));
    }, 2800);
  };

  const fetchTasks = useCallback(async (activeStatus) => {
    setLoading(true);
    setError('');
    try {
      const [filteredData, fullData] = await Promise.all([
        getTasks(activeStatus),
        activeStatus ? getTasks(null) : Promise.resolve(null),
      ]);
      setTasks(filteredData);
      setAllTasks(activeStatus ? fullData : filteredData);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchTasks(filterStatus);
  }, [filterStatus, fetchTasks]);

  const handleCreateTask = async (payload) => {
    try {
      await createTask(payload);
      await fetchTasks(filterStatus);
      showNotice('Đã thêm công việc mới.');
    } catch (err) {
      throw extractErrorMessage(err);
    }
  };

  const handleStatusChange = async (id, newStatus) => {
    try {
      await updateTaskStatus(id, newStatus);
      await fetchTasks(filterStatus);
      showNotice('Đã cập nhật trạng thái.');
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  const handleDeleteTask = async (id) => {
    try {
      await deleteTask(id);
      await fetchTasks(filterStatus);
      showNotice('Đã xóa công việc.');
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  return (
    <div className="min-h-screen bg-stone-50">
      <header className="border-b border-stone-200/80 bg-white">
        <div className="mx-auto flex max-w-5xl items-center justify-between px-4 py-4 sm:px-6">
          <div className="flex items-center gap-3">
            <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-stone-900 text-white">
              <ClipboardList className="h-5 w-5" />
            </div>
            <div>
              <h1 className="text-base font-bold tracking-tight text-stone-900 sm:text-lg">
                Mini Task &amp; Issue Tracker
              </h1>
              <p className="text-xs text-stone-500">
                Spring Boot REST API · React Dashboard
              </p>
            </div>
          </div>

          <div className="flex items-center gap-2.5">
            <span
              className={`hidden items-center gap-1.5 rounded-full border px-2.5 py-1 text-xs font-medium sm:inline-flex ${
                error
                  ? 'border-amber-200 bg-amber-50 text-amber-800'
                  : 'border-emerald-200 bg-emerald-50 text-emerald-700'
              }`}
            >
              <span
                className={`h-1.5 w-1.5 rounded-full ${
                  error ? 'bg-amber-500' : 'bg-emerald-500'
                }`}
              />
              {error ? 'Mất kết nối API' : 'API :6971 sẵn sàng'}
            </span>

            <button
              type="button"
              onClick={() => fetchTasks(filterStatus)}
              disabled={loading}
              className="inline-flex items-center gap-1.5 rounded-xl border border-stone-200 bg-white px-3 py-1.5 text-xs font-medium text-stone-700 transition hover:bg-stone-50 disabled:opacity-50 cursor-pointer"
            >
              <RefreshCw
                className={`h-3.5 w-3.5 ${loading ? 'animate-spin' : ''}`}
              />
              Làm mới
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-5xl space-y-6 px-4 py-6 sm:px-6">
        <StatsBar tasks={allTasks} />

        {notice && (
          <div
            role="status"
            className="flex items-center gap-2 rounded-xl border border-teal-200 bg-teal-50 px-4 py-2.5 text-xs font-medium text-teal-800"
          >
            <CheckCircle2 className="h-4 w-4 shrink-0" />
            <span>{notice}</span>
          </div>
        )}

        {error && (
          <div
            role="alert"
            className="flex items-start justify-between gap-3 rounded-2xl border border-amber-200 bg-amber-50 p-4 text-sm text-amber-900"
          >
            <div className="flex items-start gap-2.5">
              <AlertTriangle className="mt-0.5 h-4 w-4 shrink-0 text-amber-600" />
              <div>
                <p className="font-semibold">Không thể tải dữ liệu từ server</p>
                <p className="mt-0.5 text-xs text-amber-800">{error}</p>
              </div>
            </div>
            <button
              type="button"
              onClick={() => fetchTasks(filterStatus)}
              className="shrink-0 rounded-lg border border-amber-300 bg-white px-3 py-1.5 text-xs font-medium text-amber-900 hover:bg-amber-100 cursor-pointer"
            >
              Thử lại
            </button>
          </div>
        )}

        <div className="grid gap-6 lg:grid-cols-12">
          <div className="lg:col-span-4">
            <div className="lg:sticky lg:top-6">
              <TaskForm onCreateTask={handleCreateTask} />
            </div>
          </div>

          <section className="space-y-4 lg:col-span-8">
            <div className="flex flex-wrap items-center justify-between gap-3">
              <div
                className="inline-flex rounded-xl border border-stone-200/90 bg-white p-1 shadow-xs"
                role="tablist"
                aria-label="Lọc theo trạng thái"
              >
                {FILTER_TABS.map((tab) => {
                  const isActive = filterStatus === tab.value;
                  return (
                    <button
                      key={tab.label}
                      type="button"
                      role="tab"
                      aria-selected={isActive}
                      onClick={() => setFilterStatus(tab.value)}
                      className={`rounded-lg px-3 py-1.5 text-xs font-medium transition cursor-pointer ${
                        isActive
                          ? 'bg-stone-900 text-white'
                          : 'text-stone-600 hover:text-stone-900'
                      }`}
                    >
                      {tab.label}
                    </button>
                  );
                })}
              </div>

              <span className="text-xs text-stone-500">
                Hiển thị <strong className="text-stone-800">{tasks.length}</strong>{' '}
                công việc
              </span>
            </div>

            {loading ? (
              <div className="space-y-3">
                {[1, 2, 3].map((item) => (
                  <div
                    key={item}
                    className="h-32 animate-pulse rounded-2xl border border-stone-200/80 bg-white p-4"
                  />
                ))}
              </div>
            ) : tasks.length === 0 ? (
              <div className="rounded-2xl border border-dashed border-stone-300 bg-white px-6 py-12 text-center">
                <div className="mx-auto flex h-11 w-11 items-center justify-center rounded-2xl bg-stone-100 text-stone-500">
                  <ClipboardList className="h-5 w-5" />
                </div>
                <h3 className="mt-3 text-sm font-semibold text-stone-900">
                  {filterStatus
                    ? 'Chưa có công việc nào ở mục này'
                    : 'Chưa có công việc nào'}
                </h3>
              </div>
            ) : (
              <div className="space-y-3">
                {tasks.map((task) => (
                  <TaskCard
                    key={task.id}
                    task={task}
                    onStatusChange={handleStatusChange}
                    onDelete={handleDeleteTask}
                  />
                ))}
              </div>
            )}
          </section>
        </div>
      </main>
    </div>
  );
}
