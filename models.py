"""Data models for the BAP + QCAP port terminal optimization."""

from dataclasses import dataclass, field


@dataclass
class Vessel:
    """A vessel (ship) that needs berth allocation and crane assignment."""

    name: str
    workload: int  # M_i: total container movements needed
    loa: int  # Length Over All in meters
    draft: float  # Required depth for mooring
    productivity: int  # Movements per crane per shift
    etw: int  # Earliest shift the vessel can arrive (0-indexed)
    etc: int  # Desired completion shift (0-indexed, exclusive)
    available_shifts: list[int] | None = None  # If None, available all shifts
    available_shifts: list[int] | None = None  # If None, available all shifts
    max_cranes: int = 3  # Max cranes that fit on the vessel


@dataclass
class ForbiddenZone:
    """A zone on the quay that is restricted during certain shifts (e.g. maintenance)."""

    start_berth_position: int
    end_berth_position: int
    start_shift: int
    end_shift: int  # Exclusive
    description: str = "Maintenance"



@dataclass
class Berth:
    """The quay/berth where vessels are moored."""

    length: int  # Total length in meters
    depth: float | None = None  # Uniform depth (if constant)
    depth_map: dict[int, float] | None = None  # Position -> depth (variable)

    def get_depth_at(self, position: int) -> float:
        """Return the depth at a given position along the berth."""
        if self.depth is not None:
            return self.depth
        if self.depth_map is not None:
            # Find the depth segment that covers this position
            sorted_positions = sorted(self.depth_map.keys())
            result = 0.0
            for p in sorted_positions:
                if p <= position:
                    result = self.depth_map[p]
                else:
                    break
            return result
        return float("inf")


@dataclass
class Problem:
    """Full problem instance for BAP + QCAP optimization."""

    berth: Berth
    vessels: list[Vessel]
    num_shifts: int  # Total number of shifts in the planning horizon
    num_shifts: int  # Total number of shifts in the planning horizon
    total_cranes_per_shift: list[int] | None = None  # Cranes available each shift
    forbidden_zones: list[ForbiddenZone] = field(default_factory=list)

    def __post_init__(self):
        if self.total_cranes_per_shift is None:
            # Default: same number of cranes every shift
            self.total_cranes_per_shift = [10] * self.num_shifts


@dataclass
class VesselSolution:
    """Solution for a single vessel."""

    vessel_name: str
    berth_position: int  # Starting meter on the berth
    start_shift: int
    end_shift: int  # Exclusive
    cranes_per_shift: dict[int, int] = field(default_factory=dict)  # shift -> cranes


@dataclass
class Solution:
    """Complete solution for the BAP + QCAP problem."""

    vessel_solutions: list[VesselSolution]
    objective_value: float
    status: str  # "OPTIMAL", "FEASIBLE", "INFEASIBLE", etc.
