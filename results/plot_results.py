import csv
from collections import defaultdict
from pathlib import Path
from statistics import median

import matplotlib.pyplot as plt


ROOT = Path(__file__).resolve().parent
PLOTS = ROOT.parent / "photos"
PLOTS.mkdir(exist_ok=True)

groups = defaultdict(list)
with (ROOT / "results.csv").open(newline="", encoding="utf-8") as source:
    for row in csv.DictReader(source):
        groups[row["algorithm"], row["input"], int(row["n"])].append(row)


def series(algorithm, input_kind, metric):
    points = sorted(
        (n, values)
        for (name, kind, n), values in groups.items()
        if name == algorithm and kind == input_kind
    )
    if metric == "time_ms":
        return [n for n, _ in points], [median(float(r[metric]) for r in values) for _, values in points]
    return [n for n, _ in points], [int(values[0][metric]) for _, values in points]


def save(name, title, xlabel, ylabel):
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.grid(alpha=0.25)
    plt.legend()
    plt.tight_layout()
    plt.savefig(PLOTS / name, dpi=180)
    plt.close()


fig, axes = plt.subplots(1, 2, figsize=(11, 4))
for axis, algorithms, title in (
    (axes[0], ("recursive",), "Recursive"),
    (axes[1], ("table", "two_rows"), "Dynamic programming"),
):
    for algorithm in algorithms:
        x, y = series(algorithm, "disjoint", "operations")
        axis.plot(x, y, "o-", label=algorithm.replace("_", " "))
    axis.set_title(title)
    axis.set_xlabel("String length n (characters)")
    axis.set_ylabel("Subproblems or DP cells (count)")
    axis.set_yscale("log")
    axis.grid(alpha=0.25)
    axis.legend()
fig.suptitle("Edit Distance work: disjoint strings")
fig.tight_layout()
fig.savefig(PLOTS / "edit_work.png", dpi=180)
plt.close(fig)

for algorithm in ("table", "two_rows"):
    for kind in ("random", "disjoint"):
        x, y = series(algorithm, kind, "time_ms")
        plt.plot(x, y, "o-", label=f"{algorithm.replace('_', ' ')} / {kind}")
save("edit_time.png", "Edit Distance: DP median time", "String length n (characters)",
     "Median of 5 runs (ms)")

for algorithm in ("naive", "kmp"):
    for kind in ("random", "repeated_prefix"):
        x, y = series(algorithm, kind, "operations")
        plt.plot(x, y, "o-", label=f"{algorithm} / {kind.replace('_', ' ')}")
save("search_comparisons.png", "String search: character comparisons", "Text length n (characters)",
     "Character comparisons (count)")

for kind in ("random", "repeated_prefix"):
    x, naive = series("naive", kind, "operations")
    _, kmp = series("kmp", kind, "operations")
    plt.plot(x, [a / b for a, b in zip(naive, kmp)], "o-", label=kind.replace("_", " "))
save("search_ratio.png", "Naive / KMP comparison ratio", "Text length n (characters)",
     "Naive comparisons / KMP comparisons (ratio)")

print(f"Saved 4 plots in {PLOTS}")
